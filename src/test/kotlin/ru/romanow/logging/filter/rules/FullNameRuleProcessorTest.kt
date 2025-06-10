package ru.romanow.logging.filter.rules

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FullNameRuleProcessorTest {

    @Test
    fun testFullNameRule() {
        val processor = FullNameRuleProcessor("fullName")
        assertThat(processor.apply("\"fullName\" : \"Романов Алексей Сергеевич\""))
            .isEqualTo("\"fullName\" : \"Р****** Алексей Се***ч\"")
        assertThat(processor.apply("\"fullName\" : \"Гогия Владимир\""))
            .isEqualTo("\"fullName\" : \"Г**** Владимир\"")
        assertThat(processor.apply("\"fullName\" : \"Услан Ы Оглы Викторович\""))
            .isEqualTo("\"fullName\" : \"У**** Ы О*** Ви***ч\"")
    }

    @Test
    fun testFullNameRuleRegex() {
        val processor = FullNameRuleProcessor("role\\d{4,8}")
        assertThat(processor.apply("{\"role4210\": \"Романов А.С.\", \"role12420\": \"Романова Е.В.\"}"))
            .isEqualTo("{\"role4210\": \"Р****** А.С.\", \"role12420\": \"Ро***а Е.В.\"}")
    }
}
