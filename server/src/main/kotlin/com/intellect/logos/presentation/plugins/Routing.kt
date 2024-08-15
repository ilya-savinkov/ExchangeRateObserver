package com.intellect.logos.presentation.plugins

import ServerConstant
import com.intellect.logos.presentation.controller.assetController
import com.intellect.logos.presentation.controller.exchangeController
import io.ktor.server.application.Application
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route(ServerConstant.API) {
            route(ServerConstant.API_V1) {
                exchangeController()
                assetController()
            }
        }

        staticResources(
            remotePath = "/image",
            basePackage = "static/image"
        ) {
            preCompressed(CompressedFileType.GZIP)
        }
    }
}