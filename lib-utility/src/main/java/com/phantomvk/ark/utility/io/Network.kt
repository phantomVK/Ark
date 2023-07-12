@file:JvmName("NetworkKt")

package com.phantomvk.ark.utility.io

import com.phantomvk.ark.utility.sys.CmdResult
import com.phantomvk.ark.utility.sys.CmdStatus
import com.phantomvk.ark.utility.sys.execCmd


/**
 * Ping the given domain.
 *
 * @param domain The domain to ping
 * @param count count
 * @param interval second
 * @param timeOut second
 */
fun pingDomain(
  domain: String,
  count: Int,
  interval: String,
  timeOut: String
): CmdResult {
  return execCmd("ping -c $count -i $interval -W $timeOut $domain")
}

fun isPingSuccess(
  domain: String,
  count: Int,
  interval: String,
  timeOut: String
): Boolean {
  val status = pingDomain(domain, count, interval, timeOut)
  return (status.status == CmdStatus.SUCCESS)
    && (status.msg.count { it.contains("ttl=", true) } == count)
}
