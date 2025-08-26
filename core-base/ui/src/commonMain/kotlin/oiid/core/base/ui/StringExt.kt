package oiid.core.base.ui

/**
 * Extension property that returns a string with the first letter of each word capitalized.
 * For example, "hello world" becomes "Hello World".
 */
val String.capitalizeEachWord: String
    get() = this.split(" ").joinToString(" ") { word ->
        word.takeIf { it.isNotEmpty() }
            ?.let { it.first().uppercase() + it.substring(1).lowercase() }
            ?: ""
    }
