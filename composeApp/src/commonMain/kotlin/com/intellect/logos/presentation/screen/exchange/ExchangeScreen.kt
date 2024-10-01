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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModelUDF.Action
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeComponent
import com.intellect.logos.presentation.screen.exchange.component.exchange.composable.ExchangeComposable
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardComponent
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardComposable
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.exchange
import exchangerateobserver.composeapp.generated.resources.failed_to_load_assets
import exchangerateobserver.composeapp.generated.resources.settings
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExchangeScreen(
    viewModel: ExchangeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                is ExchangeViewModelUDF.Event.FailedToLoadAssets -> {
                    snackbarHostState.showSnackbar(
                        message = getString(Res.string.failed_to_load_assets)
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
            exchangeComponent = viewModel.exchangeComponent,
            keyboardComponent = viewModel.keyboardComponent,
            snackbarHostState = snackbarHostState,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ExchangeRateContent(
    exchangeComponent: ExchangeComponent,
    keyboardComponent: KeyboardComponent,
    snackbarHostState: SnackbarHostState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        ExchangeComposable(
            component = exchangeComponent,
            snackbarHostState = snackbarHostState,
            animatedVisibilityScope = animatedVisibilityScope
        )

        Spacer(modifier = Modifier.weight(1f))
        KeyboardComposable(keyboardComponent)
        Spacer(modifier = Modifier.height(16.dp))
    }
}