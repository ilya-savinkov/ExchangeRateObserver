package com.intellect.logos

import ServerConstant
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
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