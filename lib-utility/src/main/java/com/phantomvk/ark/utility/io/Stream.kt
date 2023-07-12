@file:JvmName("StreamKt")

package com.phantomvk.ark.utility.io


import com.phantomvk.ark.utility.sys.postOnMainThread
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream


fun writeStream(
  `is`: InputStream,
  os: OutputStream,
  callback: ((writeBytes: Long) -> Unit)? = null
): Boolean {
  val buffer = ByteArray(8192)
  var read: Int

  if (callback == null) {
    while (`is`.read(buffer).also { read = it } != -1) {
      os.write(buffer, 0, read)
    }
  } else {
    var writeBytes = 0L
    while (`is`.read(buffer).also { read = it } != -1) {
      os.write(buffer, 0, read)
      writeBytes += read
      postOnMainThread { callback.invoke(writeBytes) }
    }
  }

  return true
}

fun <T> readStream(
  `is`: InputStream,
  callback: ((reader: BufferedReader) -> T)
): T {
  return InputStreamReader(`is`).use { streamReader ->
    BufferedReader(streamReader).use { reader ->
      callback.invoke(reader)
    }
  }
}

fun readLine(`is`: InputStream): String {
  return readStream(`is`) { reader ->
    var char: Int
    val line = StringBuilder()
    while (reader.read().also { char = it } > 0) {
      line.append(char.toChar())
    }
    line.toString()
  }
}

fun readLines(`is`: InputStream): List<String> {
  return readStream(`is`) { reader ->
    var line: String? = null
    val lines = ArrayList<String>()
    while (reader.readLine()?.also { line = it } != null) {
      lines.add(line!!)
    }
    lines
  }
}
