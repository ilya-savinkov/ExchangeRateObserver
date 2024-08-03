package com.intellect.logos

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.usecase.settings.GetThemeStateFlowUseCase
import com.intellect.logos.presentation.screen.assets.AssetsScreen
import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeScreen
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModel
import com.intellect.logos.presentation.screen.settings.SettingsScreen
import com.intellect.logos.presentation.screen.settings.SettingsViewModel
import com.intellect.logos.presentation.theme.AppTheme
import io.kamel.image.config.LocalKamelConfig
import org.koin.compose.KoinContext
import org.koin.compose.getKoin


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
    // TODO Добавить splash screen

    KoinContext {
        val theme by getKoin().get<GetThemeStateFlowUseCase>().invoke().collectAsState()

        AppTheme(
            isDarkTheme = when (theme) {
                Theme.System -> isSystemInDarkTheme()
                Theme.Dark -> true
                Theme.Light -> false
            }
        ) {
            CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
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

                        composable(SettingsViewModel.ROUTE) {
                            SettingsScreen(
                                viewModel = koinViewModel(
                                    navController = navController,
                                    savedStateHandle = it.savedStateHandle
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}