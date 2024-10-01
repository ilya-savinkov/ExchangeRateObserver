package com.intellect.logos

import io.github.aakira.napier.Napier
import io.kamel.core.config.Core
import io.kamel.core.config.DefaultHttpCacheSize
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpUrlFetcher
import io.kamel.core.config.takeFrom
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

val kamelConfig: KamelConfig = KamelConfig {
    takeFrom(KamelConfig.Core)
    svgCacheSize = 300

    httpUrlFetcher {
        httpCache(DefaultHttpCacheSize)

        Logging {
            level = LogLevel.INFO
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(
                        message = message,
                        tag = "kamel"
                    )
                }
            }
        }
    }
}