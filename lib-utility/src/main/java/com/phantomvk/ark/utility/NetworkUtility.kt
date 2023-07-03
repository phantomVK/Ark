package com.phantomvk.ark.utility

import java.io.BufferedReader
import java.io.InputStreamReader

object NetworkUtility {

  /**
   * Ping the given domain.
   *
   * @param domain The domain to ping
   * @param count count
   * @param interval second
   * @param timeOut second
   */
  @JvmStatic
  fun pingDomain(domain: String, count: Int, interval: Int, timeOut: Int): PingResult {
    val result = ArrayList<String>()

    try {
      val process = Runtime.getRuntime().exec("ping -c $count -i $interval -W $timeOut $domain")

      process.inputStream.use { stream ->
        InputStreamReader(stream).use { streamReader ->
          BufferedReader(streamReader).use { reader ->
            var line: String? = null
            while (reader.readLine()?.also { line = it } != null) {
              result.add(line!!)
            }
          }
        }
      }

      if (result.toString().isNotBlank()) {
        try {
          process.destroy()
        } catch (ignore: Exception) {
        }

        return PingResult(PingStatus.SUCCESS, result)
      }

      result.clear()

      process.errorStream.use { stream ->
        InputStreamReader(stream).use { streamReader ->
          BufferedReader(streamReader).use { reader ->
            var line: String? = null
            while (reader.readLine()?.also { line = it } != null) {
              result.add(line!!)
            }
          }
        }
      }

      try {
        process.destroy()
      } catch (ignore: Exception) {
      }

      return PingResult(PingStatus.ERROR, result)
    } catch (ignore: Exception) {
      return PingResult(PingStatus.UNKNOWN, result)
    }
  }

  @JvmStatic
  fun isPingSuccess(domain: String, count: Int, interval: Int, timeOut: Int): Boolean {
    val status = pingDomain(domain, count, interval, timeOut)
    return (status.status == PingStatus.SUCCESS)
      && (status.msg.count { it.contains("ttl=", true) } == count)
  }
}

data class PingResult(
  val status: PingStatus,
  val msg: List<String>
)

enum class PingStatus {
  UNKNOWN,
  SUCCESS,
  ERROR
}