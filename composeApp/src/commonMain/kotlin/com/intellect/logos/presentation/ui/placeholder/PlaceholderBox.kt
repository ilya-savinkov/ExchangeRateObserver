package com.intellect.logos.presentation.ui.placeholder

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun PlaceholderBox(
    isLoading: Boolean,
    width: Dp = Dp.Unspecified,
    height: Dp,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier
    ) {
        if (it) {
            val widthModifier = if (width == Dp.Unspecified) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.width(width)
            }

            Box(
                modifier = widthModifier
                    .height(height)
                    .background(
                        color = Color.LightGray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .shimmerAnimation()
            )
        } else {
            content()
        }
    }
}