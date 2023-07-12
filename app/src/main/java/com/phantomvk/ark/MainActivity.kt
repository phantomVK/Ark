package com.phantomvk.ark

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

  private companion object {
    private const val REQ_EXTERNAL_STORAGE = 0x01
  }

  private lateinit var progressBar: ProgressBar
  private lateinit var textView: TextView
  private lateinit var permissionButton: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initViews()
  }

  private fun initViews() {
    progressBar = findViewById(R.id.progressBar)
    textView = findViewById(R.id.textView)

    permissionButton = findViewById(R.id.Permissions)
    permissionButton.setOnClickListener { requestPermissions() }
  }

  private fun requestPermissions() {
    val p = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (p == PackageManager.PERMISSION_DENIED) {
      ActivityCompat.requestPermissions(
        this, arrayOf(
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE
        ), REQ_EXTERNAL_STORAGE
      )
    } else {
      Toast.makeText(this, "WRITE_EXTERNAL_STORAGE granted.", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQ_EXTERNAL_STORAGE) {
      val tips = if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
        "Granted: WRITE_EXTERNAL_STORAGE"
      } else {
        "Denied: WRITE_EXTERNAL_STORAGE"
      }

      Toast.makeText(this@MainActivity, tips, Toast.LENGTH_SHORT).show()
    }
  }
}