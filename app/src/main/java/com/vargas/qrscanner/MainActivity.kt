package com.vargas.qrscanner

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vargas.qrscanner.views.ScannerActivity
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private val CAMERA_CODE=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            askCameraPermission()
        }
        else{
            initViews()
            initComponents()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_CODE && grantResults.isNotEmpty()){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                initViews()
                initComponents()
            }
            else Toast.makeText(this@MainActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.CAMERA),CAMERA_CODE)
    }

    private fun initViews(){
        this.startButton=findViewById(R.id.startButton)

    }

    private fun initComponents(){
        this.startButton.setOnClickListener{
            startActivity(Intent(this@MainActivity,ScannerActivity::class.java))

        }
    }
}