package com.education.core_api

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilTest {

    @Test
    fun timeConvert_Test() {
        val expectedTimeLong = 1584223133000 // 2020-03-14 21:58:53
        val timeStr = "2020-03-14 21:58:53 UTC"

        val actualTimeLong = convertTime(timeStr)

        assertEquals(expectedTimeLong, actualTimeLong)
    }
}