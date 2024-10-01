package com.intellect.logos.presentation.screen.assets.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.Asset
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AssetItem(
    asset: Asset,
    searchQuery: String,
    onClick: (Asset) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                text = buildHighlightAnnotatedString(
                    fullText = asset.name,
                    highlightText = searchQuery,
                    highlightColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(asset.name),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        },
        supportingContent = {
            Text(
                text = buildHighlightAnnotatedString(
                    fullText = asset.description,
                    highlightText = searchQuery,
                    highlightColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        leadingContent = {
            KamelImage(
                resource = { asyncPainterResource(asset.icon) },
                contentDescription = asset.name,
                modifier = Modifier
                    .size(48.dp)
                    // TODO Анимация только для выбранного элемента
                    .sharedElement(
                        state = rememberSharedContentState(asset.icon),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        },
        modifier = modifier
            .clickable { onClick(asset) }
    )
}

private fun buildHighlightAnnotatedString(
    fullText: String,
    highlightText: String,
    highlightColor: Color,
): AnnotatedString {
    if (highlightText.isEmpty()) {
        return AnnotatedString(
            text = fullText
        )
    }

    return buildAnnotatedString {
        var textIndex = 0

        while (textIndex < fullText.length) {
            val startIndex = fullText.indexOf(highlightText, textIndex, ignoreCase = true)

            if (startIndex == -1) {
                append(fullText.substring(textIndex, fullText.length))
                break
            } else {
                append(fullText.substring(textIndex, startIndex))

                withStyle(SpanStyle(color = highlightColor)) {
                    append(fullText.substring(startIndex, startIndex + highlightText.length))
                }

                textIndex = startIndex + highlightText.length
            }
        }
    }
}