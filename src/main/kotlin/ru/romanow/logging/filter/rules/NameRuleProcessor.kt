package ru.romanow.logging.filter.rules

import kotlin.text.RegexOption.IGNORE_CASE

open class NameRuleProcessor(field: String?) : BaseRuleProcessor(NAME_REGEX.format(field).toRegex(IGNORE_CASE)) {
    override fun mask(text: String): String {
        val length = text.length
        return if (length > 7) {
            text.substring(0..1) + "***" + text[length - 1].toString()
        } else {
            text.replaceRange(1 until length, "*".repeat(length - 1))
        }
    }

    companion object {
        const val NAME_REGEX = "\"%s\"\\s*:\\s*\"([^\"@]+)\""
    }
}
