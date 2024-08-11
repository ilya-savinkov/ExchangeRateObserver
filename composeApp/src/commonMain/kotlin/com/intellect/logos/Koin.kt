package com.intellect.logos

import ServerConstant
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.intellect.logos.data.datasource.asset.AssetLocalDataSource
import com.intellect.logos.data.datasource.asset.AssetRemoteDataSource
import com.intellect.logos.data.datasource.exchange.ExchangeLocalDataSource
import com.intellect.logos.data.datasource.exchange.ExchangeRemoteDataSource
import com.intellect.logos.data.datasource.settings.SettingsLocalDataSource
import com.intellect.logos.data.db.AppDatabase
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.getRoomDatabase
import com.intellect.logos.data.repository.AssetsRepositoryImpl
import com.intellect.logos.data.repository.ExchangeRepositoryImpl
import com.intellect.logos.data.repository.SettingsRepositoryImpl
import com.intellect.logos.domain.repository.AssetsRepository
import com.intellect.logos.domain.repository.ExchangeRepository
import com.intellect.logos.domain.repository.SettingsRepository
import com.intellect.logos.domain.usecase.assets.GetAssetUseCase
import com.intellect.logos.domain.usecase.assets.GetAssetsUseCase
import com.intellect.logos.domain.usecase.assets.GetDefaultAssetsUseCase
import com.intellect.logos.domain.usecase.assets.LoadAssetsUseCase
import com.intellect.logos.domain.usecase.assets.SetDefaultAssetUseCase
import com.intellect.logos.domain.usecase.assets.SwapAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetRateUseCase
import com.intellect.logos.domain.usecase.settings.language.ChangeLanguageUseCase
import com.intellect.logos.domain.usecase.settings.language.GetDefaultLanguageUseCase
import com.intellect.logos.domain.usecase.settings.language.GetLanguageStateFlowUseCase
import com.intellect.logos.domain.usecase.settings.theme.ChangeThemeUseCase
import com.intellect.logos.domain.usecase.settings.theme.GetDefaultThemeUseCase
import com.intellect.logos.domain.usecase.settings.theme.GetThemeStateFlowUseCase
import com.intellect.logos.domain.usecase.volume.CalculateVolumeUseCase
import com.intellect.logos.domain.usecase.volume.GetVolumeUseCase
import com.intellect.logos.presentation.router.AssetsRouterImpl
import com.intellect.logos.presentation.router.ExchangeRouterImpl
import com.intellect.logos.presentation.router.SettingsRouterImpl
import com.intellect.logos.presentation.screen.assets.AssetsRouter
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter
import com.intellect.logos.presentation.screen.settings.SettingsRouter
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.currentKoinScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

// TODO Перейти на использование аннотаций
val exchangeRateModule: Module = module {
    singleOf(::ExchangeRemoteDataSource)
    singleOf(::ExchangeLocalDataSource)
    singleOf(::ExchangeRepositoryImpl) bind ExchangeRepository::class
    factoryOf(::GetRateUseCase)
    factoryOf(::CalculateVolumeUseCase)
    factoryOf(::GetVolumeUseCase)
    factoryOf(::ExchangeRouterImpl) bind ExchangeRouter::class
}

val assetsModule: Module = module {
    singleOf(::AssetRemoteDataSource)
    singleOf(::AssetLocalDataSource)
    singleOf(::AssetsRepositoryImpl) bind AssetsRepository::class
    factoryOf(::GetAssetsUseCase)
    factoryOf(::GetAssetUseCase)
    factoryOf(::LoadAssetsUseCase)
    factoryOf(::GetDefaultAssetsUseCase)
    factoryOf(::SetDefaultAssetUseCase)
    factoryOf(::SwapAssetsUseCase)
    factoryOf(::AssetsRouterImpl) bind AssetsRouter::class
}

val settingsModule: Module = module {
    singleOf(::SettingsLocalDataSource)
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
    factoryOf(::GetDefaultThemeUseCase)
    factoryOf(::GetThemeStateFlowUseCase)
    factoryOf(::ChangeThemeUseCase)
    factoryOf(::GetDefaultLanguageUseCase)
    factoryOf(::GetLanguageStateFlowUseCase)
    factoryOf(::ChangeLanguageUseCase)
    factoryOf(::SettingsRouterImpl) bind SettingsRouter::class
}

val defaultJson: Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

val dbModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    single<AssetDao> { get<AppDatabase>().assetDao() }
}

val apiModule = module {
    single<Json> { defaultJson }

    single<HttpClient> {
        HttpClient {
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTP
                    host = ServerConstant.HOST
                    port = ServerConstant.PORT
                    path("api/")
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(
                            message = message,
                            tag = "httpClient"
                        )
                    }
                }
                level = LogLevel.BODY
            }

            install(ContentNegotiation) {
                json(get())
            }
        }
    }
}

expect class KoinInitializer {
    val viewModelModule: Module
    fun init()
}

@Composable
inline fun <reified T : ViewModel> koinViewModel(
    navController: NavController,
    savedStateHandle: SavedStateHandle,
): T {
    val scope = currentKoinScope()

    return viewModel {
        scope.get<T> {
            parametersOf(
                navController,
                savedStateHandle
            )
        }
    }
}