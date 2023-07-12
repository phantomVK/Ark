package com.phantomvk.ark.utility.callback

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.annotation.MainThread

object ActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

  private val list = ArrayList<ActivityLifecycleCallbacks>()

  @MainThread
  @JvmStatic
  fun addCallback(callback: ActivityLifecycleCallbacks) {
    list.add(callback)
  }

  @MainThread
  @JvmStatic
  fun removeCallback(callback: ActivityLifecycleCallbacks) {
    list.remove(callback)
  }

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    list.forEach { it.onActivityCreated(activity, savedInstanceState) }
  }

  override fun onActivityStarted(activity: Activity) {
    list.forEach { it.onActivityStarted(activity) }
  }

  override fun onActivityResumed(activity: Activity) {
    list.forEach { it.onActivityResumed(activity) }
  }

  override fun onActivityPaused(activity: Activity) {
    list.forEach { it.onActivityPaused(activity) }
  }

  override fun onActivityStopped(activity: Activity) {
    list.forEach { it.onActivityStopped(activity) }
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    list.forEach { it.onActivitySaveInstanceState(activity, outState) }
  }

  override fun onActivityDestroyed(activity: Activity) {
    list.forEach { it.onActivityDestroyed(activity) }
  }
}
