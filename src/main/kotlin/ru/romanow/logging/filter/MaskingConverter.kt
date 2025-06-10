package ru.romanow.logging.filter

import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.pattern.ConverterKeys
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter
import org.apache.logging.log4j.core.pattern.PatternFormatter
import org.yaml.snakeyaml.Yaml
import ru.romanow.logging.filter.rules.*
import ru.romanow.logging.properties.MaskingProperties
import ru.romanow.logging.properties.RuleType.*

@ConverterKeys("mask")
@Plugin(name = "MaskingConverter", category = "Converter")
class MaskingConverter(
    private val formatters: MutableList<PatternFormatter>
) : LogEventPatternConverter("mask", "mask") {

    private var rules = mutableListOf<RuleProcessor>()

    init {
        val properties = loadRules("logging/rules.yml")!!
        val additionalProperties = loadRules("logging/additional-rules.yml")
        if (additionalProperties != null) {
            properties.masking?.addAll(additionalProperties.masking!!)
        }

        for ((type, field, regex) in properties.masking!!) {
            val ruleProcessor = when (type!!) {
                TEXT -> TextRuleProcessor(field, regex)
                EMAIL -> EmailRuleProcessor(field)
                NAME -> NameRuleProcessor(field)
                FULL_NAME -> FullNameRuleProcessor(field)
            }
            rules.add(ruleProcessor)
        }
    }

    override fun format(event: LogEvent, toAppendTo: StringBuilder) {
        val buffer = StringBuilder()
        for (formatter in formatters) {
            formatter.format(event, buffer)
        }
        val message = mask(buffer.toString())
        toAppendTo.append(message)
    }

    fun mask(message: String): String {
        var result = message
        for (rule in rules) {
            result = rule.apply(result)
        }
        return result
    }

    private fun loadRules(location: String): MaskingProperties? {
        val stream = object {}.javaClass.classLoader.getResourceAsStream(location)
        return if (stream != null) {
            Yaml().loadAs(stream, MaskingProperties::class.java)
        } else {
            null
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(config: Configuration, options: Array<String>): MaskingConverter {
            val parser = PatternLayout.createPatternParser(config)
            val formatters = parser.parse(options[0])
            return MaskingConverter(formatters)
        }
    }
}
