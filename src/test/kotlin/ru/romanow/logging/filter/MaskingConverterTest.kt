package ru.romanow.logging.filter

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class MaskingConverterTest {
    private var maskingConverter = MaskingConverter(mutableListOf())

    @Test
    fun `when Message with sensitive data Then mask`() {
        val result = maskingConverter.mask(LOG_MESSAGE)
        assertThat(result).isEqualTo(MASKED_MESSAGE)
    }

    @Suppress("ktlint:standard:max-line-length")
    companion object {
        private const val LOG_MESSAGE = "{\"person\": {\"firstName\": \"Алексей\", \"surname\": \"Сергеевич\", \"lastName\": \"Романов\", \"fullName\": \"Романов Алексей Сергеевич\", \"fullNameShort\": \"Романов А.С.\", \"email\": \"romanowalex@mail.ru\", \"department\": {\"fullname\": \"Research & Development\"}, \"createdBy\": \"Романов А.С.\", \"deputies\": {\"role4210\": \"Романов А.С.\", \"role12420\": \"Романова Е.В.\"}}}"
        private const val MASKED_MESSAGE = "{\"person\": {\"firstName\": \"Алексей\", \"surname\": \"Се***ч\", \"lastName\": \"Р******\", \"fullName\": \"Р****** Алексей Се***ч\", \"fullNameShort\": \"Р****** А.С.\", \"email\": \"r**********@mail.ru\", \"department\": {\"fullname\": \"Re***h & De***t\"}, \"createdBy\": \"Р****** А.С.\", \"deputies\": {\"role4210\": \"Р****** А.С.\", \"role12420\": \"Ро***а Е.В.\"}}}"
    }
}
