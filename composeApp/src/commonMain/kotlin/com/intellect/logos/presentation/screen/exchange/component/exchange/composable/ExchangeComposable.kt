package com.intellect.logos.presentation.screen.exchange.component.exchange.composable

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeComponent
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.failed_to_load_rate
import exchangerateobserver.composeapp.generated.resources.ic_swap
import exchangerateobserver.composeapp.generated.resources.swap
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExchangeComposable(
    component: ExchangeComponent,
    snackbarHostState: SnackbarHostState,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    LaunchedEffect(Unit) {
        component.event.onEach { event ->
            when (event) {
                is ExchangeUDF.Event.FailedToLoadRate -> {
                    snackbarHostState.showSnackbar(
                        message = getString(Res.string.failed_to_load_rate)
                    )
                }
            }
        }.launchIn(this)
    }

    Column {
        val baseAsset by component.state.baseAsset.collectAsStateWithLifecycle()

        AssetInputComposable(
            assetState = baseAsset,
            onClick = {
                component.onAction(
                    ExchangeUDF.Action.TapAsset(
                        type = Asset.Type.Base
                    )
                )
            },
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_swap),
                contentDescription = stringResource(Res.string.swap),
                modifier = Modifier
                    .padding(start = 24.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            component.onAction(ExchangeUDF.Action.Swap)
                        }
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            val rate by component.state.rate.collectAsStateWithLifecycle()

            RateComposable(
                rateState = rate,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        val quoteAsset by component.state.quoteAsset.collectAsStateWithLifecycle()

        AssetInputComposable(
            assetState = quoteAsset,
            onClick = {
                component.onAction(
                    ExchangeUDF.Action.TapAsset(
                        type = Asset.Type.Quote
                    )
                )
            },
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(16.dp)
        )
    }
}