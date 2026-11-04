package com.zebra.datacapturedemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zebra.ai.datacapture.EntityTrackerAnalyzer
import com.zebra.ai.datacapture.EntityViewfinder
import com.zebra.ai.datacapture.detectors.BarcodeDecoder
import com.zebra.ai.datacapture.detectors.TextOCR

class MainActivity : AppCompatActivity() {
    private lateinit var viewfinder: EntityViewfinder
    private lateinit var entityTracker: EntityTrackerAnalyzer
    private val CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewfinder = findViewById(R.id.entityViewfinder)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initSdk()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CODE)
        }
    }

    private fun initSdk() {
        try {
            val decoder = BarcodeDecoder.Builder(this).enableAllSymbologies().build()
            val ocr = TextOCR.Builder(this).setOCRPicklist(true).build()
            entityTracker = EntityTrackerAnalyzer.Builder(this).addDetector(decoder).addDetector(ocr).build()
            entityTracker.bindToLifecycle(this)
            viewfinder.setController(entityTracker.viewFinderController)
        } catch (e: Exception) {
            Log.e("ZebraAI", "Erro", e)
        }
    }
}
