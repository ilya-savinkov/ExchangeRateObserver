package com.intellect.logos.presentation.screen.exchange.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.ui.placeholder.PlaceholderBox
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.decodeURLPart

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AssetInputComponent(
    asset: Asset,
    isLoading: Boolean,
    onClick: (asset: Asset) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    PlaceholderBox(
        isLoading = isLoading,
        height = 100.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium)
                .clickable { onClick(asset) }
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                Text(
                    text = asset.currency.code,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(asset.currency.code),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
//                text = asset.volume,
                text = "0.0",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}