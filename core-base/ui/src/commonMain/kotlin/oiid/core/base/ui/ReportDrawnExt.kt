package oiid.core.base.ui

import androidx.compose.runtime.Composable

/**
 * Reports to the composition system that content is considered drawn when the specified condition is true.
 * Platform-specific implementation that affects rendering optimizations.
 *
 * @param block Lambda that returns true when content should be considered drawn
 */
@Composable
expect fun ReportDrawnWhen(block: () -> Boolean)
