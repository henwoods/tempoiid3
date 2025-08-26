package oiid.core.base.ui

import androidx.compose.runtime.Composable

@Composable
actual fun ReportDrawnWhen(block: () -> Boolean) {
    androidx.activity.compose.ReportDrawnWhen { block() }
}
