package com.education.core_api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

const val notConvertedUtcTime = "2020-03-14 21:58:53 UTC"
const val convertedTimeToLong = 1584223133000 // 2020-03-14 21:58:53 UTC

class UtilTestJUnit5 {

    @DisplayName("Convert Time")
    @ParameterizedTest(name = "\"{0}\" should be {1}")
    @CsvSource(value = [
        "$notConvertedUtcTime, $convertedTimeToLong", // Success
        "'', 0", // wrong format
        "'2020-03-14-ff 21:58:53 UTC', 0" // wrong format
    ])
    fun convertTimeTest(input: String, expected: Long) {
        val actualValue = convertTime(input)

        assertThat(actualValue).isEqualTo(expected)
    }
}
