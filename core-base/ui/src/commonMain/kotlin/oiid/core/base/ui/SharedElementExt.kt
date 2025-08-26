@file:OptIn(ExperimentalSharedTransitionApi::class)

package oiid.core.base.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal that provides access to an [AnimatedVisibilityScope] within the composition.
 * Default value is null, requiring an explicit provider upstream in the composition.
 */
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * CompositionLocal that provides access to a [SharedTransitionScope] within the composition.
 * Used for creating shared element transitions between composables.
 * Default value is null, requiring an explicit provider upstream in the composition.
 *
 * Note: Uses experimental API [ExperimentalSharedTransitionApi].
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
