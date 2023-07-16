@file:JvmName("ImageKt")

package com.phantomvk.ark.utility.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import androidx.exifinterface.media.ExifInterface
import com.phantomvk.ark.utility.io.checkDstFile
import com.phantomvk.ark.utility.io.writeFile
import java.io.File


fun getImageSize(path: String): Point {
  val options = BitmapFactory.Options().apply {
    inJustDecodeBounds = true
  }

  BitmapFactory.decodeFile(path, options)

  val rotation = getImageRotation(path)
  return if ((rotation % 180) == 0) {
    Point(options.outWidth, options.outHeight)
  } else {
    Point(options.outHeight, options.outWidth)
  }
}

fun getImageRotation(path: String): Int {
  val attr = ExifInterface(path).getAttributeInt(
    ExifInterface.TAG_ORIENTATION,
    ExifInterface.ORIENTATION_NORMAL
  )

  return when (attr) {
    ExifInterface.ORIENTATION_ROTATE_90 -> 90
    ExifInterface.ORIENTATION_ROTATE_180 -> 180
    ExifInterface.ORIENTATION_ROTATE_270 -> 270
    else -> 0
  }
}

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