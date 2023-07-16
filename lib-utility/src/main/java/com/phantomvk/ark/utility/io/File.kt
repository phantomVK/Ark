@file:JvmName("FileKt")

package com.phantomvk.ark.utility.io

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets


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
  callback: ((progress: Double, writtenBytes: Int, totalBytes: Int) -> Unit)? = null
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
  callback: ((progress: Double, writtenBytes: Int, totalBytes: Int) -> Unit)? = null
): Boolean {
  if (!checkSrcFile(srcFile) || !checkDstFile(dstFile)) {
    return false
  }

  FileInputStream(srcFile).use { fis ->
    return writeFile(fis, dstFile, callback)
  }
}

fun checkSrcFile(file: File): Boolean {
  return file.exists() && file.canRead() && file.isFile
}

fun checkDstFile(file: File): Boolean {
  if (file.exists()) {
    if (!file.delete()) {
      return false
    }
  }

  return file.mkdirs()
}
