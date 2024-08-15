package com.intellect.logos.presentation.controller

import com.intellect.logos.domain.usecase.asset.GetAssetUseCase
import com.intellect.logos.domain.usecase.asset.GetAssetsUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject

fun Route.assetController() {
    val getAssetsUseCase by inject<GetAssetsUseCase>()
    val getAssetUseCase by inject<GetAssetUseCase>()

    get("assets") {
        val page = call.request.queryParameters.getOrFail("page").toLong()
        val pageSize = call.request.queryParameters.getOrFail("pageSize").toInt()

        val assets = getAssetsUseCase(
            page = page,
            pageSize = pageSize
        )

        call.respond(assets)
    }

    get("asset/{name}") {
        val name = call.parameters.getOrFail("name")
        val asset = getAssetUseCase(name)

        if (asset == null) {
            // TODO Create common error response
            call.respond("Asset not found")
        } else {
            call.respond(asset)
        }
    }
}