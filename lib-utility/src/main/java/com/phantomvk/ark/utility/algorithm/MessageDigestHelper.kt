@file:JvmName("MessageDigestKt")

package com.phantomvk.ark.utility.algorithm

import com.phantomvk.ark.utility.sys.postOnMainThread
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.util.Formatter


private const val BUFFER_SIZE = 8192

/**
 * 根据文件内容计算哈希
 *
 * @param pathname  文件路径
 * @param algorithm java.security.MessageDigest
 */
fun getFileHash(
  pathname: String,
  algorithm: String,
  callback: ((readBytes: Long) -> Unit)?
): String {
  val file = File(pathname)
  val md = MessageDigest.getInstance(algorithm)

  FileInputStream(file).use { fis ->
    digestStream(md, fis, callback)
  }

  return bytesToHexString(md.digest())
}

fun digestStream(
  md: MessageDigest,
  `is`: InputStream,
  callback: ((readBytes: Long) -> Unit)?
) {
  val buffer = ByteArray(BUFFER_SIZE)
  var read: Int

  if (callback == null) {
    while (`is`.read(buffer, 0, BUFFER_SIZE).also { read = it } != -1) {
      md.update(buffer, 0, read)
    }
  } else {
    var readBytes = 0L
    while (`is`.read(buffer, 0, BUFFER_SIZE).also { read = it } != -1) {
      md.update(buffer, 0, read)
      readBytes += read
      postOnMainThread { callback.invoke(readBytes) }
    }
  }
}

fun bytesToHexString(
  byteArray: ByteArray
): String {
  val formatter = Formatter()
  for (byte in byteArray) {
    formatter.format("%02x", byte)
  }

  return formatter.toString()
}
