package com.phantomvk.ark.utility

import android.os.StatFs
import java.io.File

object SystemUtility {

  /**
   * Get directory available capacity.
   *
   * @return available capacity in bytes
   */
  @JvmStatic
  fun getDirectoryAvailableCapacity(dir: File): Long {
    val stat = StatFs(dir.path)
    return stat.blockSizeLong * stat.availableBlocksLong
  }
}