package ru.romanow.logging.filter.rules

abstract class BaseRuleProcessor(private val regex: Regex) : RuleProcessor {
    override fun apply(text: String): String {
        var offset = 0
        var result = text
        for (match in regex.findAll(text)) {
            val group = match.groups[1]!!
            val replacement = mask(group.value)
            result = result.replaceRange(group.range.first - offset, group.range.last + 1 - offset, replacement)
            offset += (group.value.length - replacement.length)
        }
        return result
    }

    protected abstract fun mask(text: String): String
}
