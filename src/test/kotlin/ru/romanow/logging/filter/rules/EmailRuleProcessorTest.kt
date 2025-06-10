package ru.romanow.logging.filter.rules

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EmailRuleProcessorTest {

    @Test
    fun testEmailRule() {
        val processor = EmailRuleProcessor("email")
        assertThat(processor.apply("\"email\" : \"romanowalex@mail.ru\""))
            .isEqualTo("\"email\" : \"r**********@mail.ru\"")
        assertThat(processor.apply("\"email\" : \"aleksey.romanov@inno.tech\""))
            .isEqualTo("\"email\" : \"a**************@inno.tech\"")
    }
}
