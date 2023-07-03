package com.phantomvk.ark.utility

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T : Any> T.deepCopy(gson: Gson = GsonProvider.getInstance()): T {
  return gson.fromJson(gson.toJson(this), object : TypeToken<T>() {}.type)
}

inline fun <reified T : Any> List<T>.deepCopy(gson: Gson = GsonProvider.getInstance()): ArrayList<T> {
  return map { it.deepCopy(gson) }.toCollection(ArrayList(size))
}
