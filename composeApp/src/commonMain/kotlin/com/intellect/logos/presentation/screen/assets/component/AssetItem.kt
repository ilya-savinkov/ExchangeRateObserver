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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.Asset
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.decodeURLPart

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
                    fullText = asset.currency.code,
                    highlightText = searchQuery,
                    highlightColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(asset.currency.code),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        },
        supportingContent = {
            val description = remember(Locale.current.language) {
                asset.currency.description.getOrElse(Locale.current.language) {
                    asset.currency.description.getValue("en")
                }
            }

            Text(
                text = buildHighlightAnnotatedString(
                    fullText = description,
                    highlightText = searchQuery,
                    highlightColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        leadingContent = {
            KamelImage(
                resource = asyncPainterResource(asset.icon.decodeURLPart()),
                contentDescription = asset.currency.code,
                modifier = Modifier
                    .size(48.dp)
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