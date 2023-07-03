package com.phantomvk.ark.utility.android

import android.os.Handler
import android.os.Looper


private val mainHandler = Handler(Looper.getMainLooper())

fun postOnMainThread(delayMillis: Long = 0L, runnable: Runnable) {
  mainHandler.postDelayed(runnable, delayMillis)
}

fun removeCallbacks(runnable: Runnable) {
  mainHandler.removeCallbacks(runnable)
}
