package com.phantomvk.ark.utility

import com.google.gson.Gson

object GsonProvider {

  @Volatile
  private var sGson: Gson? = null

  private val defaultGson by lazy { Gson() }

  @Synchronized
  fun init(gson: Gson) {
    if (sGson != null) {
      throw RuntimeException("Should init only once.")
    } else {
      sGson = gson
    }
  }

  fun getInstance(): Gson {
    return sGson ?: defaultGson
  }
}