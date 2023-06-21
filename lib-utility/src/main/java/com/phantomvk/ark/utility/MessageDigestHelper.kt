package com.phantomvk.ark.utility

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*

object MessageDigestHelper {

  private const val BUFFER_SIZE = 8192

  /**
   * 根据文件内容计算哈希
   *
   * @param pathname  文件路径
   * @param algorithm java.security.MessageDigest
   */
  @JvmStatic
  fun getFileHash(
    pathname: String,
    algorithm: String,
    callback: ((readBytes: Long) -> Unit)?
  ): String {
    val file = File(pathname)
    val md = MessageDigest.getInstance(algorithm)

    FileInputStream(file).use { fis ->
      val buffer = ByteArray(BUFFER_SIZE)
      var read: Int

      if (callback == null) {
        while (fis.read(buffer, 0, BUFFER_SIZE).also { read = it } != -1) {
          md.update(buffer, 0, read)
        }
      } else {
        var readBytes = 0L
        while (fis.read(buffer, 0, BUFFER_SIZE).also { read = it } != -1) {
          md.update(buffer, 0, read)
          readBytes += read
          callback.invoke(readBytes)
        }
      }
    }

    val byteArray = md.digest()
    val formatter = Formatter()
    for (byte in byteArray) {
      formatter.format("%02x", byte)
    }

    return formatter.toString()
  }
}