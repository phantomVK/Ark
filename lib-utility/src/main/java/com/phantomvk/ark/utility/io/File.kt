@file:JvmName("FileKt")

package com.phantomvk.ark.utility.io

import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets


fun writeBitmap(
  bitmap: Bitmap,
  dstFile: File,
  compressFormat: Bitmap.CompressFormat,
  quality: Int
): Boolean {
  if (!checkDstFile(dstFile)) {
    return false
  }

  return writeFile(dstFile) {
    bitmap.compress(compressFormat, quality, this)
  }
}

fun writeString(
  string: String,
  dstFile: File
): Boolean {
  if (!checkDstFile(dstFile)) {
    return false
  }

  return writeFile(dstFile) {
    write(string.toByteArray(StandardCharsets.UTF_8))
    true
  }
}

fun writeFile(
  dstFile: File,
  callback: (OutputStream.() -> Boolean)
): Boolean {
  if (!checkDstFile(dstFile)) {
    return false
  }

  FileOutputStream(dstFile).use { fos ->
    BufferedOutputStream(fos).use { bos ->
      return callback.invoke(bos)
    }
  }
}

fun writeFile(
  `is`: InputStream,
  dstFile: File,
  callback: ((writeBytes: Long) -> Unit)? = null
): Boolean {
  if (!checkDstFile(dstFile)) {
    return false
  }

  FileOutputStream(dstFile).use { fos ->
    return writeStream(`is`, fos, callback)
  }
}

fun copyFile(
  srcFile: File,
  dstFile: File,
  callback: ((writeBytes: Long) -> Unit)? = null
): Boolean {
  if (!checkSrcFile(srcFile) || !checkDstFile(dstFile)) {
    return false
  }

  FileInputStream(srcFile).use { fis ->
    return writeFile(fis, dstFile, callback)
  }
}

private fun checkSrcFile(file: File): Boolean {
  return file.exists() && file.canRead() && file.isFile
}

private fun checkDstFile(file: File): Boolean {
  if (file.exists()) {
    if (!file.delete()) {
      return false
    }
  } else {
    val parentFile = file.parentFile
    if (parentFile == null || (!parentFile.exists() && !parentFile.mkdirs())) {
      return false
    }
  }

  return true
}