@file:JvmName("SystemKt")

package com.phantomvk.ark.utility.sys

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.os.StatFs
import java.io.File
import java.io.FileInputStream
import com.phantomvk.ark.utility.io.readLine
import com.phantomvk.ark.utility.io.readLines


/**
 * Get directory available capacity.
 *
 * @return available capacity in bytes
 */
fun getDirectoryAvailableCapacity(dir: File): Long {
  val stat = StatFs(dir.path)
  return stat.blockSizeLong * stat.availableBlocksLong
}

/**
 * Current processor name.
 */
@Volatile
private var sProcessName: String? = null

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
    (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
      ?.runningAppProcesses
      ?.firstOrNull { it.pid == pid }?.processName
  } catch (ignore: Exception) {
    null
  }
}

fun getProcessorNameByCmd(): String {
  FileInputStream("/proc/self/cmdline").use { fis ->
    return readLine(fis)
  }
}

/**
 * Exec terminal command.
 */
fun execCmd(cmd: String): CmdResult {
  var process: java.lang.Process? = null

  try {
    process = Runtime.getRuntime().exec(cmd)

    val lines = readLines(process.inputStream)
    if (lines.isNotEmpty()) {
      return CmdResult(CmdStatus.SUCCESS, lines)
    }

    val errorLines = readLines(process.errorStream)
    return CmdResult(CmdStatus.ERROR, errorLines)
  } catch (e: Exception) {
    val exceptionMsg = arrayListOf("Unexpected exception found.")
    return CmdResult(CmdStatus.EXCEPTION, exceptionMsg, e)
  } finally {
    try {
      process?.destroy()
    } catch (ignore: Exception) {
    }
  }
}

enum class CmdStatus {
  EXCEPTION,
  SUCCESS,
  ERROR
}

data class CmdResult(
  val status: CmdStatus,
  val msg: List<String>,
  val exception: Exception? = null
)