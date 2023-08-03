package com.vargas.qrscanner.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ProxyFileDescriptorCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.vargas.qrscanner.R
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.util.concurrent.Executors

class ShowDialog : DialogFragment(){

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var linkText: TextView
    private lateinit var visitText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =inflater.inflate(R.layout.show_dialog,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initComponents(view:View) {
        this.titleText= view.findViewById(R.id.titleText)
        this.descriptionText= view.findViewById(R.id.descriptionText)
        this.linkText= view.findViewById(R.id.linkText)
        this.visitText= view.findViewById(R.id.visitText)
    }

    fun updateUrl(url:String, type:Int){
        if (type==1) {
            fetchUrlMetaData(url) { title, desc ->
                titleText.text = title
                descriptionText.text = desc
                linkText.text = url
                visitText.setOnClickListener {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(url)
                        startActivity(it)
                    }
                }

            }
        }
        if (type==2){
            titleText.text = "BarCode"
            descriptionText.text = url
            linkText.text = ""
        }
    }

    private fun fetchUrlMetaData(url: String, callback: (title:String,desc:String)->Unit) {
        val executor= Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute(Runnable {
            val doc= Jsoup.connect(url).get()
            val desc= doc.select("meta[name=description]")[0].attr("content")
            handler.post{
                callback(doc.title(),doc.wholeText().toString())
            }
        })
    }
}