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
import androidx.navigation.toRoute
import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.usecase.settings.theme.GetThemeStateFlowUseCase
import com.intellect.logos.presentation.screen.assets.AssetsScreen
import com.intellect.logos.presentation.screen.exchange.ExchangeScreen
import com.intellect.logos.presentation.screen.settings.SettingsScreen
import com.intellect.logos.presentation.theme.AppTheme
import io.kamel.image.config.LocalKamelConfig
import org.koin.compose.KoinContext
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun App() {
    // TODO Добавить поддержку планшетов
    // TODO Добавить поддержку часов
    // TODO Добавить splash screen
    // TODO Add Firebase Crashlytics

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

                    // TODO Add navigation analytics
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Exchange
                    ) {
                        composable<Screen.Exchange> {
                            ExchangeScreen(
                                viewModel = koinNavViewModel { parametersOf(navController) },
                                animatedVisibilityScope = this
                            )
                        }

                        composable<Screen.Assets> {
                            val screen = it.toRoute<Screen.Assets>()
                            val assetType = screen.assetType

                            AssetsScreen(
                                viewModel = koinNavViewModel {
                                    parametersOf(
                                        navController,
                                        assetType
                                    )
                                },
                                animatedVisibilityScope = this
                            )
                        }

                        composable<Screen.Settings> {
                            SettingsScreen(
                                viewModel = koinNavViewModel { parametersOf(navController) },
                            )
                        }
                    }
                }
            }
        }
    }
}