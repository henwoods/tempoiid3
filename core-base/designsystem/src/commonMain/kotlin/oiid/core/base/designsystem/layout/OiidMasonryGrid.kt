package oiid.core.base.designsystem.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastMap
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun OiidMasonryGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = OiidTheme.spacing.sm,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.testTag("OiidMasonryGrid"),
        content = content,
    ) { measurables, constraints ->
        val columnWidth = (constraints.maxWidth - spacing.roundToPx() * (columns - 1)) / columns
        val itemConstraints = constraints.copy(
            minWidth = columnWidth,
            maxWidth = columnWidth,
        )

        val placeables = measurables.fastMap { it.measure(itemConstraints) }
        val columnHeights = IntArray(columns) { 0 }

        val itemPlacements = placeables.fastMap { placeable ->
            val shortestColumnIndex = columnHeights.withIndex().minBy { it.value }.index
            val x = shortestColumnIndex * (columnWidth + spacing.roundToPx())
            val y = columnHeights[shortestColumnIndex]

            columnHeights[shortestColumnIndex] += placeable.height + spacing.roundToPx()

            Pair(x, y)
        }

        val totalHeight = columnHeights.maxOrNull() ?: 0

        layout(constraints.maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val (x, y) = itemPlacements[index]
                placeable.place(x, y)
            }
        }
    }
}
