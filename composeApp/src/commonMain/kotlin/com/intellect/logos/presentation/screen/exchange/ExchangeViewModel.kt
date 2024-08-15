package com.intellect.logos.presentation.screen.exchange

import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.format
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.empty
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.model.exchange.default
import com.intellect.logos.domain.model.exchange.empty
import com.intellect.logos.domain.usecase.assets.GetDefaultAssetsFlowUseCase
import com.intellect.logos.domain.usecase.assets.SwapAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetRateUseCase
import com.intellect.logos.domain.usecase.volume.GetVolumeFlowUseCase
import com.intellect.logos.domain.usecase.volume.UpdateVolumeUseCase
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Event
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.State
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.koin.android.annotation.KoinViewModel

// TODO Add unit tests
@KoinViewModel
class ExchangeViewModel(
    private val getDefaultAssetsFlowUseCase: GetDefaultAssetsFlowUseCase,
    private val swapAssetsUseCase: SwapAssetsUseCase,
    private val getVolumeFlowUseCase: GetVolumeFlowUseCase,
    private val updateVolumeUseCase: UpdateVolumeUseCase,
    private val getRateUseCase: GetRateUseCase,
    private val router: ExchangeRouter
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        isLoadingAssets = true,
        isLoadingRate = true,
        baseAsset = Asset.empty(),
        quoteAsset = Asset.empty(),
        volume = Volume.default(),
        convertedVolume = "1",
        rate = Rate.empty()
    )
) {
    override suspend fun onInit() {
        subscribeAssets()
    }

    private suspend fun subscribeAssets() {
        combine(
            getDefaultAssetsFlowUseCase(),
            getVolumeFlowUseCase()
        ) { defaultAssets, volume ->
            defaultAssets.onSuccess { (baseAsset, quoteAsset) ->
                val baseAssetName = baseAsset.name
                val quoteAssetName = quoteAsset.name

                getRateUseCase(
                    baseAssetName = baseAssetName,
                    quoteAssetName = quoteAssetName
                ).onSuccess { rate ->
                    setState {
                        copy(
                            isLoadingAssets = false,
                            isLoadingRate = false,
                            baseAsset = baseAsset,
                            quoteAsset = quoteAsset,
                            volume = volume,
                            convertedVolume = (volume.value * rate).toString().format(),
                            rate = Rate(
                                assets = baseAssetName to quoteAssetName,
                                volume = rate
                            )
                        )
                    }
                }.onFailure {
                    // TODO Send all errors to analytics
                    Napier.e(it) { "ExchangeRateViewModel" }
                    sendEvent(Event.FailedToLoadRate)
                }
            }.onFailure {
                // TODO Send all errors to analytics
                Napier.e(it) { "ExchangeRateViewModel" }
                sendEvent(Event.FailedToLoadAssets)
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            is Action.TapAsset -> tapAsset(action.type)
            is Action.TapKey -> tapKey(action.key)
            Action.Swap -> swap()
            Action.OpenSettings -> router.openSettings()
        }
    }

    private suspend fun tapKey(key: Key) {
        if (currentState.isLoadingAssets) return

        updateVolumeUseCase(
            asset = currentState.baseAsset,
            volume = currentState.volume,
            key = key
        )
    }

    private suspend fun swap() {
        if (currentState.isLoadingAssets) return
        swapAssetsUseCase()
    }

    private fun tapAsset(assetType: Asset.Type) {
        if (currentState.isLoadingAssets) return

        router.openAssets(
            assetType = assetType
        )
    }
}