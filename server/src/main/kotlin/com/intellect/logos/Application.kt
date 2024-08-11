package com.intellect.logos

import ServerConstant
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail
import kotlinx.serialization.json.Json
import response.RateResponse

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

    install(StatusPages) {
        exception<MissingRequestParameterException> { call, cause ->
            call.respondText(
                text = "Missing \"${cause.parameterName}\" parameter.",
                status = HttpStatusCode.BadRequest
            )
        }

        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    routing {
        get("/api/rate") {
            val baseAssetName = call.request.queryParameters.getOrFail("baseAssetName")
            val quoteAssetName = call.request.queryParameters.getOrFail("quoteAssetName")

            call.respond(
                RateResponse(
                    baseAssetName = baseAssetName,
                    quoteAssetName = quoteAssetName,
                    rate = 2.1
                )
            )
        }

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
