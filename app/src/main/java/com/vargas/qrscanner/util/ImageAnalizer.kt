package com.vargas.qrscanner.util

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vargas.qrscanner.views.ShowDialog

class ImageAnalizer (private val fragmentManager: FragmentManager):ImageAnalysis.Analyzer{
    private val showDialog = ShowDialog()
    
    override fun analyze(images: ImageProxy){
        scanBarCode(images)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarCode(images: ImageProxy) {
        images.image?.let{
            image ->
            val inputImage = InputImage.fromMediaImage(image,images.imageInfo.rotationDegrees)
            val scanner= BarcodeScanning.getClient()

            scanner.process(inputImage).addOnCompleteListener{
                images.close()
                if (it.isSuccessful){
                    readBarCode(it.result as List<Barcode>)
                }
                else{
                    it.exception?.printStackTrace()
                }
            }
        }
    }

    private fun readBarCode(barcodes: List<Barcode>) {
        for (barcode in barcodes){
            when (barcode.valueType){
                Barcode.TYPE_URL->{
                    if (!showDialog.isAdded){
                        showDialog.show(fragmentManager,"")
                        showDialog.updateUrl(barcode.url?.url.toString(),1)
                    }
                }
                else->{
                    if (!showDialog.isAdded){
                        showDialog.show(fragmentManager,"")
                        showDialog.updateUrl(barcode.rawValue.toString(),2)
                    }
                }
            }
        }
    }
}