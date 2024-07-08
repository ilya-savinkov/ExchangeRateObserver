package com.intellect.logos

import ServerConstant
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.intellect.logos.data.repository.AssetsRepositoryImpl
import com.intellect.logos.data.repository.ExchangeRatesRepositoryImpl
import com.intellect.logos.domain.repository.AssetsRepository
import com.intellect.logos.domain.repository.ExchangeRatesRepository
import com.intellect.logos.domain.usecase.assets.GetAssetUseCase
import com.intellect.logos.domain.usecase.assets.GetAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetExchangeRatesUseCase
import com.intellect.logos.presentation.router.AssetsRouterImpl
import com.intellect.logos.presentation.router.ExchangeRouterImpl
import com.intellect.logos.presentation.screen.assets.AssetsRouter
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.compose.currentKoinScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

val exchangeRateModule: Module = module {
    singleOf(::ExchangeRatesRepositoryImpl) bind ExchangeRatesRepository::class
    factoryOf(::GetExchangeRatesUseCase)
    factoryOf(::ExchangeRouterImpl) bind ExchangeRouter::class
}

val assetsModule: Module = module {
    singleOf(::AssetsRepositoryImpl) bind AssetsRepository::class
    factoryOf(::GetAssetsUseCase)
    factoryOf(::GetAssetUseCase)
    factoryOf(::AssetsRouterImpl) bind AssetsRouter::class
}

val defaultJson: Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
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