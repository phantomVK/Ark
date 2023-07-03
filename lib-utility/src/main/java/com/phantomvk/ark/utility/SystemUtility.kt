package com.phantomvk.ark.utility

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.os.StatFs
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

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

  /**
   * Current processor name.
   */
  private var sProcessName: String? = null

  @JvmStatic
  fun getProcessorName(context: Context): String? {
    if (!sProcessName.isNullOrBlank()) {
      return sProcessName
    }

    sProcessName = getProcessNameByPid(context)
    if (sProcessName.isNullOrBlank()) {
      return sProcessName
    }

    sProcessName = getProcessorNameByCmd()

    return sProcessName
  }

  fun getProcessNameByPid(context: Context): String? {
    return try {
      val pid = Process.myPid()
      (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.runningAppProcesses?.firstOrNull { it.pid == pid }?.processName
    } catch (ignore: Exception) {
      null
    }
  }

  fun getProcessorNameByCmd(): String? {
    return try {
      FileInputStream("/proc/self/cmdline").use { fis ->
        InputStreamReader(fis).use { isr ->
          BufferedReader(isr).use { reader ->
            val processName = StringBuilder()
            var char: Int
            while (reader.read().also { char = it } > 0) {
              processName.append(char.toChar())
            }
            processName.toString()
          }
        }
      }
    } catch (ignore: Exception) {
      null
    }
  }

  fun pingDomain(
    domain: String,
    count: Int,
    interval: Int,
    timeOut: Int
  ): String {
    try {
      val process = Runtime.getRuntime().exec("ping -c $count -i $interval -W $timeOut $domain")

      val success = process.inputStream.use { stream ->
        InputStreamReader(stream).use { streamReader ->
          BufferedReader(streamReader).use { reader ->
            var lines: String? = null
            val result = StringBuilder()

            while (reader.readLine()?.also { lines = it } != null) {
              result.append(lines).append('\n')
            }

            result
          }
        }
      }

      val error = process.errorStream.use { stream ->
        InputStreamReader(stream).use { streamReader ->
          BufferedReader(streamReader).use { reader ->
            var lines: String? = null
            val result = StringBuilder()

            while (reader.readLine()?.also { lines = it } != null) {
              result.append(lines).append('\n')
            }

            result
          }
        }
      }

      try {
        process.destroy()
      } catch (ignore: Exception) {
      }

      return "$success     $error"
    } catch (ignore: Exception) {
    }

    return "UNKNOWN"
  }
}