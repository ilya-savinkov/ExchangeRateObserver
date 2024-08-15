package com.intellect.logos

import ServerConstant
import com.intellect.logos.data.db.AppDatabase
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.dao.RateDao
import com.intellect.logos.data.db.getRoomDatabase
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
import org.koin.core.annotation.ComponentScan
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

@org.koin.core.annotation.Module
@ComponentScan("com.intellect.logos")
class AppModule

val defaultJson: Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

val dbModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    single<AssetDao> { get<AppDatabase>().assetDao() }
    single<RateDao> { get<AppDatabase>().rateDao() }
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