package com.intellect.logos.presentation.screen.exchange

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.State
import com.intellect.logos.presentation.screen.exchange.component.AssetInputComponent
import com.intellect.logos.presentation.screen.exchange.component.KeyboardComponent
import com.intellect.logos.presentation.screen.exchange.component.RateComponent
import com.intellect.logos.presentation.screen.exchange.model.AssetType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExchangeScreen(
    viewModel: ExchangeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Exchange" // TODO get from resources
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // TODO navigate to settings
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings" // TODO get from resources
                        )
                    }
                }
            )
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
            asset = state.baseAsset,
            isLoading = state.isLoadingAssets,
            onClick = {
                onAction(
                    Action.TapAsset(
                        asset = state.baseAsset,
                        assetType = AssetType.Base
                    )
                )
            },
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(16.dp)
        )

        // TODO swap button

        RateComponent(
            rate = state.rate,
            isLoading = state.isLoadingRate,
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.End)
        )

        AssetInputComponent(
            asset = state.quoteAsset,
            isLoading = state.isLoadingAssets,
            onClick = {
                onAction(
                    Action.TapAsset(
                        asset = state.quoteAsset,
                        assetType = AssetType.Quote
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