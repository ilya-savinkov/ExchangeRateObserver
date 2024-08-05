package com.intellect.logos

import ServerConstant
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        factory = Netty,
        port = ServerConstant.PORT,
        host = ServerConstant.HOST,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    routing {
        staticResources(
            remotePath = "/api/assets",
            basePackage = "static/json",
            index = "assets.json"
        ) {
            preCompressed(CompressedFileType.GZIP)
            contentType { ContentType.Application.Json }
        }

        staticResources(
            remotePath = "/image",
            basePackage = "static/image"
        ) {
            preCompressed(CompressedFileType.GZIP)
        }
    }
}
