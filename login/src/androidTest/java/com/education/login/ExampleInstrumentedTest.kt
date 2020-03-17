package com.education.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.education.login.test", appContext.packageName)
    }

    @Test
    fun timeConvert_Test() {
        val expectedTime = "21:58:53"
        val onlyTimeFormat = "HH:mm:ss"
        val timeStr = "2020-03-14 18:58:53 UTC"
        val timeFormat = "yyyy-MM-dd HH:mm:ss"

        val sdf = SimpleDateFormat(timeFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val sdfOutPutToSend = SimpleDateFormat(onlyTimeFormat)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()

        val gmt = sdf.parse(timeStr)
        val dateToAssert = sdfOutPutToSend.format(gmt)

        assertEquals(dateToAssert, expectedTime)
    }
}
