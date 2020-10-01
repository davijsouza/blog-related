package com.phauer.unittestkotlin

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParseTestKotest {

    @Test
    fun `parse valid tokens`() {
        parse("1511443755_2") shouldBe Token(1511443755, "2")
        parse("151175_13521") shouldBe Token(151175, "13521")
        parse("151144375_id") shouldBe Token(151144375, "id")
        parse("15114437599_12") shouldBe Token(15114437599, "1")
        parse(null) shouldBe null
    }

    @ParameterizedTest
    @MethodSource("validTokenProvider")
    fun `parse valid tokens`(data: TestData) {
        parse(data.input) shouldBe data.expected
    }

    private fun validTokenProvider() = Stream.of(
        TestData(input = "1511443755_2", expected = Token(1511443755, "2")),
        TestData(input = "151175_13521", expected = Token(151175, "13521")),
        TestData(input = "151144375_id", expected = Token(151144375, "id")),
        TestData(input = "15114437599_1", expected = Token(15114437599, "1")),
        TestData(input = null, expected = null)
    )
}
