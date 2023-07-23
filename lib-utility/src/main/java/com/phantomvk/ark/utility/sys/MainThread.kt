@file:JvmName("MainThreadKt")

package com.phantomvk.ark.utility.sys

import android.os.Handler
import android.os.Looper

/**
 * Android main thread handler.
 */
internal val mainHandler = Handler(Looper.getMainLooper())

/**
 * Post a runnable to main thread handler with delay.
 */
fun postOnMainThread(delayMillis: Long = 0L, runnable: () -> Unit) {
  mainHandler.postDelayed(runnable, delayMillis)
}

fun assertRunOnMainThread() {
  if (Looper.myLooper() != Looper.getMainLooper()) {
    throw RuntimeException("Should run on the main thread.")
  }
}
