package com.phantomvk.ark.utility

import com.phantomvk.ark.utility.android.postOnMainThread
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.nio.charset.StandardCharsets


object FileUtility {

  @JvmStatic
  fun writeString(
    string: String,
    dstFile: File
  ): Boolean {
    return write(dstFile) {
      write(string.toByteArray(StandardCharsets.UTF_8))
      true
    }
  }

  fun write(
    dstFile: File,
    callback: (OutputStream.() -> Boolean)
  ): Boolean {
    if (!checkFile(dstFile)) {
      return false
    }

    return try {
      FileOutputStream(dstFile).use { fos ->
        BufferedOutputStream(fos).use { bos ->
          callback.invoke(bos)
        }
      }
    } catch (e: Exception) {
      false
    }
  }

  private fun checkFile(dstFile: File): Boolean {
    if (dstFile.exists()) {
      if (!dstFile.delete()) {
        return false
      }
    } else {
      val parentFile = dstFile.parentFile
      if (parentFile == null || (!parentFile.exists() && !parentFile.mkdirs())) {
        return false
      }
    }

    return true
  }

  @JvmStatic
  fun copyFile(
    srcFile: File,
    dstFile: URI,
    callback: ((writeBytes: Long) -> Unit)? = null
  ): Boolean {
    if (srcFile.exists() || !srcFile.canRead() || srcFile.isDirectory) {
      return false
    }

    return try {
      FileInputStream(srcFile).use { fis ->
        FileOutputStream(File(dstFile)).use { fos ->
          writeStream(fis, fos, callback)
        }
      }
    } catch (e: Exception) {
      false
    }
  }

  @JvmStatic
  fun writeStream(
    `is`: InputStream,
    dstFile: File,
    callback: ((writeBytes: Long) -> Unit)? = null
  ): Boolean {
    return try {
      FileOutputStream(dstFile).use { fos ->
        writeStream(`is`, fos, callback)
      }
    } catch (e: Exception) {
      false
    }
  }

  @JvmStatic
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
}