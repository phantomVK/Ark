package com.phantomvk.ark

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.phantomvk.ark.utility.NetworkUtility.isPingSuccess
import com.phantomvk.ark.utility.android.postOnMainThread
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<Button>(R.id.button).setOnClickListener {
      thread {
//        val result = NetworkUtility.pingDomain("www.baidu.com", 3, 1, 3).toString()
        val result = isPingSuccess("www.baidu.com", 3, 1, 1)
        postOnMainThread {
          findViewById<TextView>(R.id.textView).text = result.toString()
        }
      }
    }
  }
}