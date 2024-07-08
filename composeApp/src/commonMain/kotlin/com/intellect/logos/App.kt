package com.intellect.logos

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.intellect.logos.presentation.screen.assets.AssetsScreen
import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeScreen
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModel
import com.intellect.logos.presentation.theme.AppTheme
import io.kamel.image.config.LocalKamelConfig
import org.koin.compose.KoinContext


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
    AppTheme {
        CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
            KoinContext {
                SharedTransitionLayout {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = ExchangeViewModel.ROUTE
                    ) {
                        composable(ExchangeViewModel.ROUTE) {
                            ExchangeScreen(
                                viewModel = koinViewModel(
                                    navController = navController,
                                    savedStateHandle = it.savedStateHandle
                                ),
                                animatedVisibilityScope = this
                            )
                        }

                        composable(AssetsViewModel.ROUTE) {
                            AssetsScreen(
                                viewModel = koinViewModel(
                                    navController = navController,
                                    savedStateHandle = it.savedStateHandle
                                ),
                                animatedVisibilityScope = this
                            )
                        }
                    }
                }
            }
        }
    }
}