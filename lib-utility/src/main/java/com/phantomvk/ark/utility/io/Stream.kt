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
  callback: ((progress: Double, writtenBytes: Int, totalBytes: Int) -> Unit)? = null
): Boolean {
  val buffer = ByteArray(8192)
  var write: Int

  if (callback == null) {
    while (`is`.read(buffer).also { write = it } != -1) {
      os.write(buffer, 0, write)
    }
  } else {
    var written = 0
    val total = `is`.available()

    while (`is`.read(buffer).also { write = it } != -1) {
      os.write(buffer, 0, write)
      written += write

      val progress = written.toDouble() / total
      postOnMainThread { callback.invoke(progress, written, total) }
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
