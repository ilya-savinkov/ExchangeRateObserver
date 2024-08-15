package com.intellect.logos.presentation.screen.exchange

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.State
import com.intellect.logos.presentation.screen.exchange.component.AssetInputComponent
import com.intellect.logos.presentation.screen.exchange.component.KeyboardComponent
import com.intellect.logos.presentation.screen.exchange.component.RateComponent
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.exchange
import exchangerateobserver.composeapp.generated.resources.failed_to_load_assets
import exchangerateobserver.composeapp.generated.resources.failed_to_load_rate
import exchangerateobserver.composeapp.generated.resources.ic_swap
import exchangerateobserver.composeapp.generated.resources.settings
import exchangerateobserver.composeapp.generated.resources.swap
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExchangeScreen(
    viewModel: ExchangeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                is ExchangeUDF.Event.FailedToLoadAssets -> {
                    snackbarHostState.showSnackbar(
                        message = getString(Res.string.failed_to_load_assets)
                    )
                }

                is ExchangeUDF.Event.FailedToLoadRate -> {
                    snackbarHostState.showSnackbar(
                        message = getString(Res.string.failed_to_load_rate)
                    )
                }
            }
        }.launchIn(this)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.exchange)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.onAction(Action.OpenSettings)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(Res.string.settings)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        ExchangeRateContent(
            state = state,
            onAction = viewModel::onAction,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ExchangeRateContent(
    state: State,
    onAction: (Action) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        // TODO Add third asset

        AssetInputComponent(
            volume = state.volume.text,
            asset = state.baseAsset,
            isLoading = state.isLoadingAssets,
            onClick = {
                onAction(
                    Action.TapAsset(
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
                            onAction(Action.Swap)
                        }
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            RateComponent(
                rate = state.rate,
                isLoading = state.isLoadingRate,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        AssetInputComponent(
            volume = state.convertedVolume,
            asset = state.quoteAsset,
            isLoading = state.isLoadingAssets,
            onClick = {
                onAction(
                    Action.TapAsset(
                        type = Asset.Type.Quote
                    )
                )
            },
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        KeyboardComponent(
            onKeyClick = {
                onAction(Action.TapKey(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}