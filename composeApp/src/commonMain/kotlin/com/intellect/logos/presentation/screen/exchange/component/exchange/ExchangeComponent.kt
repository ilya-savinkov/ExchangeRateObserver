package com.intellect.logos.presentation.screen.exchange.component.exchange

import com.intellect.logos.common.format
import com.intellect.logos.common.presentation.udf.BaseComponent
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.empty
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.usecase.assets.SwapAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetRateUseCase
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF.Event
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF.Model
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF.State
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.AssetState
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.RateState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

class ExchangeComponent(
    private val getRateUseCase: GetRateUseCase,
    private val swapAssetsUseCase: SwapAssetsUseCase,
    private val router: ExchangeRouter,
    private val model: Model,
    coroutineScope: CoroutineScope
) : BaseComponent<State, Model, Action, Event>(
    coroutineScope = coroutineScope
) {
    private val baseAsset: StateFlow<AssetState> = combine(
        model.isLoadingAssets,
        model.baseAsset,
        model.volume
    ) { isLoadingAssets, asset, volume ->
        if (isLoadingAssets) return@combine AssetState.Loading

        AssetState.Data(
            name = asset.name,
            icon = asset.icon,
            volume = volume.value.format()
        )
    }.stateIn(AssetState.Loading)

    private val quoteAsset: StateFlow<AssetState> = combine(
        model.isLoadingAssets,
        model.quoteAsset,
        model.convertedVolume
    ) { isLoadingAssets, asset, convertedVolume ->
        if (isLoadingAssets) return@combine AssetState.Loading

        AssetState.Data(
            name = asset.name,
            icon = asset.icon,
            volume = convertedVolume
        )
    }.stateIn(AssetState.Loading)

    private val rate: StateFlow<RateState> = combine(
        model.isLoadingRate,
        model.rate,
        model.baseAsset.map { it.name },
        model.quoteAsset.map { it.name },
    ) { isLoadingRate, rate, baseAssetName, quoteAssetName ->
        if (isLoadingRate) return@combine RateState.Loading

        RateState.Data(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName,
            rate = rate.value.format()
        )
    }.stateIn(RateState.Loading)

    override val state: State = State(
        baseAsset = baseAsset,
        quoteAsset = quoteAsset,
        rate = rate
    )

    init {
        @Suppress("OPT_IN_USAGE")
        combine(
            model.baseAsset.filter { it != Asset.empty() },
            model.quoteAsset.filter { it != Asset.empty() },
            model.volume.sample(250.milliseconds)
        ) { baseAsset, quoteAsset, volume ->
            updateRate(
                baseAssetName = baseAsset.name,
                quoteAssetName = quoteAsset.name,
                volume = volume.value
            )
        }
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            is Action.TapAsset -> tapAsset(action.type)
            Action.Swap -> swap()
        }
    }

    private suspend fun updateRate(
        baseAssetName: String,
        quoteAssetName: String,
        volume: Double
    ) {
        getRateUseCase(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName
        ).onSuccess { rate ->
            model.isLoadingRate.update { false }
            model.convertedVolume.update { (rate * volume).format() }

            model.rate.update {
                Rate(
                    assets = baseAssetName to quoteAssetName,
                    value = rate
                )
            }
        }.onFailure {
            Napier.e(it) { "ExchangeRateViewModel" }
            sendEvent(Event.FailedToLoadRate)
        }
    }

    private suspend fun swap() {
        if (model.isLoadingAssets.value) return
        swapAssetsUseCase()
    }

    private fun tapAsset(assetType: Asset.Type) {
        if (model.isLoadingAssets.value) return
        router.openAssets(assetType)
    }
}