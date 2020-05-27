package com.education.core_impl

import com.education.core_api.toOriginalTitleYear
import com.education.core_impl.data.local.LocalDataSourceImpl.Companion.convertTime
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

    @DisplayName("Convert Time to year string")
    @ParameterizedTest(name = "\"{0}\" should be {1}")
    @CsvSource(value = [
        "'2020-03-14', ' (2020)'", // Success
        "'1982-04-25',' (1982)'",
        "'', ''", // wrong format
        "'ff-03-14', ''" // wrong format
    ])
    fun convertTimeToYearOnlyString(input: String, expected: String) {
        val actualValue = input.toOriginalTitleYear()

        assertThat(actualValue).isEqualTo(expected)
    }
}
