package com.phantomvk.ark.utility

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI

class FileUtility {

  fun copyFile(
    srcFile: File,
    dstFile: URI,
    callback: ((writeBytes: Long) -> Unit)? = null
  ) {
    if (srcFile.exists()) {
      throw FileNotFoundException("SourceFile does not exists.")
    }

    FileInputStream(srcFile).use { fis ->
      FileOutputStream(File(dstFile)).use { fos ->
        val buffer = ByteArray(8192)
        var read: Int

        if (callback == null) {
          while (fis.read(buffer).also { read = it } != -1) {
            fos.write(buffer, 0, read)
          }
        } else {
          var writeBytes = 0L
          while (fis.read(buffer).also { read = it } != -1) {
            fos.write(buffer, 0, read)
            writeBytes += read
            callback.invoke(writeBytes)
          }
        }
      }
    }
  }

  fun writeFileStream(
    file: File,
    inputStream: InputStream,
    callback: ((writeBytes: Long) -> Unit)? = null
  ) {
    FileOutputStream(file).use { fos ->
      val buffer = ByteArray(8192)
      var read: Int

      if (callback == null) {
        while (inputStream.read(buffer).also { read = it } != -1) {
          fos.write(buffer, 0, read)
        }
      } else {
        var writeBytes = 0L
        while (inputStream.read(buffer).also { read = it } != -1) {
          fos.write(buffer, 0, read)
          writeBytes += read
          callback.invoke(writeBytes)
        }
      }
    }
  }
}