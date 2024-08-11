package com.intellect.logos.presentation.screen.exchange

import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.format
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.empty
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.empty
import com.intellect.logos.domain.usecase.assets.GetDefaultAssetsUseCase
import com.intellect.logos.domain.usecase.assets.LoadAssetsUseCase
import com.intellect.logos.domain.usecase.assets.SwapAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetRateUseCase
import com.intellect.logos.domain.usecase.volume.CalculateVolumeUseCase
import com.intellect.logos.domain.usecase.volume.GetVolumeUseCase
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Event
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.State
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

// TODO Add unit tests
class ExchangeViewModel(
    private val loadAssetsUseCase: LoadAssetsUseCase,
    private val getDefaultAssetsUseCase: GetDefaultAssetsUseCase,
    private val swapAssetsUseCase: SwapAssetsUseCase,
    private val getVolumeUseCase: GetVolumeUseCase,
    private val calculateVolumeUseCase: CalculateVolumeUseCase,
    private val getRateUseCase: GetRateUseCase,
    private val router: ExchangeRouter,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        isLoadingAssets = true,
        isLoadingRate = true,
        baseAsset = Asset.empty(),
        quoteAsset = Asset.empty(),
        volume = "1",
        convertedVolume = "1",
        rate = Rate.empty()
    )
) {
    companion object {
        const val ROUTE = "exchange"
    }

    init {
        viewModelScope.launch {
            loadAssetsUseCase().onSuccess {
                setState {
                    copy(
                        isLoadingAssets = false,
                        isLoadingRate = false
                    )
                }

                subscribeAssets()
            }.onFailure {
                // TODO Send all errors to analytics
                Napier.e(it) { "ExchangeRateViewModel" }
                sendEvent(Event.FailedToLoadAssets)
            }
        }
    }

    private suspend fun subscribeAssets() {
        combine(
            getDefaultAssetsUseCase(),
            getVolumeUseCase()
        ) { (baseAsset, quoteAsset), volume ->
            val baseAssetName = baseAsset.currency.code
            val quoteAssetName = quoteAsset.currency.code

            getRateUseCase(
                baseAssetName = baseAssetName,
                quoteAssetName = quoteAssetName
            ).onSuccess { rate ->
                setState {
                    copy(
                        baseAsset = baseAsset,
                        quoteAsset = quoteAsset,
                        volume = volume,
                        convertedVolume = (volume.toDouble() * rate).format(2),
                        rate = Rate(
                            assets = baseAssetName to quoteAssetName,
                            volume = rate
                        )
                    )
                }
            }.onFailure {
                Napier.e(it) { "ExchangeRateViewModel" }
                sendEvent(Event.FailedToLoadRate)
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            is Action.TapAsset -> tapAsset(action.asset, action.type)
            is Action.TapKey -> tapKey(action.key)
            Action.Swap -> swap()
            Action.OpenSettings -> router.openSettings()
        }
    }

    private suspend fun tapKey(key: Key) {
        if (currentState.isLoadingAssets) return

        calculateVolumeUseCase(
            volume = currentState.volume,
            key = key,
            currency = currentState.baseAsset.currency
        )
    }

    private suspend fun swap() {
        if (currentState.isLoadingAssets) return
        swapAssetsUseCase()
    }

    private fun tapAsset(asset: Asset, type: Asset.Type) {
        if (currentState.isLoadingAssets) return

        router.openAssets(
            selectedAsset = asset,
            type = type
        )
    }
}