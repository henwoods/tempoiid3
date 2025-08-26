package com.oiid.core.model

enum class ThemeBrand(val brandName: String) {
    DEFAULT("Default"),
    ANDROID("Android"),
    ;

    companion object {
        fun fromString(value: String): ThemeBrand {
            return entries.find { it.brandName.equals(value, ignoreCase = true) } ?: DEFAULT
        }
    }
}
