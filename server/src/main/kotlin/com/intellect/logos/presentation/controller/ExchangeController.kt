package com.intellect.logos.presentation.controller

import com.intellect.logos.domain.usecase.exchange.GetRateUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject
import response.RateResponse

fun Route.exchangeController() {
    val getRateUseCase by inject<GetRateUseCase>()

    get("rate") {
        val baseAssetName = call.request.queryParameters.getOrFail("baseAssetName")
        val quoteAssetName = call.request.queryParameters.getOrFail("quoteAssetName")

        getRateUseCase(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName
        ).onSuccess {
            call.respond(
                RateResponse(
                    baseAssetName = baseAssetName,
                    quoteAssetName = quoteAssetName,
                    rate = it
                )
            )
        }.onFailure {
            // TODO Create common error response
            call.respond(it.message.orEmpty())
        }
    }
}