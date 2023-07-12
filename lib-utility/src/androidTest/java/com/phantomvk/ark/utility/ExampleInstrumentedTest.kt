package com.phantomvk.ark.utility

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun testSystemUtility() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertNotEquals(0, getDirectoryAvailableCapacity(appContext.filesDir))

    // Get processor name.
    val pkgName = "com.phantomvk.ark.utility.test"
    assertEquals(pkgName, getProcessorName(appContext))
    assertEquals(pkgName, getProcessNameByPid(appContext))
    assertEquals(pkgName, getProcessorNameByCmd())
  }
}