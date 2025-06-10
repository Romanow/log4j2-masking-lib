package ru.romanow.logging.filter.rules

class FullNameRuleProcessor(field: String?) : NameRuleProcessor(field) {
    override fun mask(text: String): String {
        val parts = text.split("\\s+".toRegex())
        return if (parts.size >= 2) {
            val result = StringBuilder(super.mask(parts[0]) + " " + parts[1])
            for (i in 2 until parts.size) {
                result.append(" ").append(super.mask(parts[i]))
            }
            return result.toString()
        } else {
            super.mask(parts[0])
        }
    }
}
