package com.phantomvk.ark.utility.android

import android.graphics.Bitmap
import com.phantomvk.ark.utility.FileUtility
import java.io.File

object BitmapUtility {

  @JvmStatic
  fun writeBitmap(
    bitmap: Bitmap,
    dstFile: File,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
  ): Boolean {
    return FileUtility.write(dstFile) {
      bitmap.compress(compressFormat, quality, this)
    }
  }
}