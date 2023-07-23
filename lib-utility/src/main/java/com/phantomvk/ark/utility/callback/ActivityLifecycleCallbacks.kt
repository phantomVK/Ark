package com.phantomvk.ark.utility.callback

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.phantomvk.ark.utility.sys.assertRunOnMainThread

object ActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

  private val set = HashSet<ActivityLifecycleCallbacks>()

  fun addCallback(callback: ActivityLifecycleCallbacks) {
    assertRunOnMainThread()
    set.add(callback)
  }

  fun removeCallback(callback: ActivityLifecycleCallbacks) {
    assertRunOnMainThread()
    set.remove(callback)
  }

  fun containCallback(callback: ActivityLifecycleCallbacks): Boolean {
    assertRunOnMainThread()
    return set.contains(callback)
  }

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    set.forEach { it.onActivityCreated(activity, savedInstanceState) }
  }

  override fun onActivityStarted(activity: Activity) {
    set.forEach { it.onActivityStarted(activity) }
  }

  override fun onActivityResumed(activity: Activity) {
    set.forEach { it.onActivityResumed(activity) }
  }

  override fun onActivityPaused(activity: Activity) {
    set.forEach { it.onActivityPaused(activity) }
  }

  override fun onActivityStopped(activity: Activity) {
    set.forEach { it.onActivityStopped(activity) }
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    set.forEach { it.onActivitySaveInstanceState(activity, outState) }
  }

  override fun onActivityDestroyed(activity: Activity) {
    set.forEach { it.onActivityDestroyed(activity) }
  }
}
