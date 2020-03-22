package com.education.core_api

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


object UtilsTest : Spek({
    Feature("core-api") {
        Scenario("Success convert time") {
            var notConvertedTime = ""
            var convertedTimeLong = 0L
            val result = convertedTimeToLong // 2020-03-14 21:58:53 UTC

            Given("init notConvertedTime") {
                notConvertedTime = notConvertedUtcTime
            }

            When("start convert time") {
                convertedTimeLong = convertTime(notConvertedTime)
            }

            Then("it should return conversion result") {
                assertThat(convertedTimeLong).isEqualTo(result)
            }
        }

        Scenario("Pass wrong format of time then return 0L") {
            var notConvertedTime = ""
            var convertedTimeLong = 0L
            val result = 0L //

            Given("init notConvertedTime") {
                notConvertedTime = "2020-03-14-ff 21:58:53 UTC"
            }

            When("start convert wrong format time") {
                convertedTimeLong = convertTime(notConvertedTime)
            }

            Then("it should return 0L result") {
                assertThat(convertedTimeLong).isEqualTo(result)
            }
        }

        Scenario("Pass blank str then return 0L") {
            var notConvertedTime = ""
            var convertedTimeLong = 0L
            val result = 0L //

            Given("init notConvertedTime") {
                notConvertedTime = ""
            }

            When("start convert wrong format time") {
                convertedTimeLong = convertTime(notConvertedTime)
            }

            Then("it should return 0L result") {
                assertThat(convertedTimeLong).isEqualTo(result)
            }
        }
    }
})

const val notConvertedUtcTime = "2020-03-14 21:58:53 UTC"
const val convertedTimeToLong = 1584223133000 // 2020-03-14 21:58:53 UTC