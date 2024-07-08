package com.intellect.logos.presentation.screen.exchange

import androidx.lifecycle.viewModelScope
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.Rate
import com.intellect.logos.domain.model.empty
import com.intellect.logos.domain.usecase.assets.GetAssetUseCase
import com.intellect.logos.domain.usecase.rates.GetExchangeRatesUseCase
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.Event
import com.intellect.logos.presentation.screen.exchange.ExchangeUDF.State
import com.intellect.logos.presentation.screen.exchange.model.AssetType
import com.intellect.logos.presentation.screen.exchange.model.Key
import com.intellect.logos.presentation.udf.BaseViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ExchangeViewModel(
    private val getAssetUseCase: GetAssetUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val exchangeRouter: ExchangeRouter,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        isLoadingAssets = true,
        isLoadingRate = true,
        baseAsset = Asset.empty(),
        quoteAsset = Asset.empty(),
        rate = Rate.empty()
    )
) {
    companion object {
        const val ROUTE = "exchange"
    }

    init {
        viewModelScope.launch {
            setDefaultAssets()
        }
    }

    private suspend fun setDefaultAssets() {
        val fromAsset = getAssetUseCase("EUR").onFailure {
            Napier.e(it) { "ExchangeRateViewModel" }
            // TODO show snackbar
        }.getOrNull() ?: return


        val toAsset = getAssetUseCase("USD").onFailure {
            Napier.e(it) { "ExchangeRateViewModel" }
            // TODO show snackbar
        }.getOrNull() ?: return

        delay(5.seconds)

        setState {
            copy(
                isLoadingAssets = false,
                isLoadingRate = false,
                baseAsset = fromAsset,
                quoteAsset = toAsset
            )
        }
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            is Action.TapAsset -> tapAsset(action.asset, action.assetType)
            is Action.TapKey -> tapKey(action.key)
            Action.Swap -> swap()
        }
    }

    private fun tapKey(key: Key) {
        if (currentState.isLoadingAssets) return

        setState {
            val prevVolume = if (baseAsset.volume.startsWith("0.")) {
                baseAsset.volume
            } else {
                baseAsset.volume.trimStart('0')
            }

            val newVolume = when (key) {
                Key.Backspace -> prevVolume.dropLast(1)

                is Key.Number -> if (prevVolume.contains('.')) {
                    val decimal = prevVolume.split('.').last().length

                    if (decimal == 2) {
                        prevVolume
                    } else {
                        prevVolume + key.value.toString()
                    }
                } else {
                    prevVolume + key.value.toString()
                }

                Key.Dot -> if (prevVolume.contains('.') || baseAsset.currency.isJPY) {
                    prevVolume
                } else {
                    if (prevVolume.isEmpty()) {
                        "0."
                    } else {
                        "$prevVolume."
                    }
                }
            }.take(10)

            copy(
                baseAsset = baseAsset.copy(
                    volume = newVolume.ifEmpty { "0" }
                )
            )
        }
    }

    private fun swap() {
        if (currentState.isLoadingAssets) return

        setState {
            copy(
                baseAsset = quoteAsset,
                quoteAsset = baseAsset
            )
        }
    }

    private fun tapAsset(asset: Asset, assetType: AssetType) {
        if (currentState.isLoadingAssets) return

        viewModelScope.launch {
            exchangeRouter.openAssets(asset) { selectedAsset ->
                changeAsset(
                    asset = selectedAsset,
                    assetType = assetType
                )
            }
        }
    }

    private fun changeAsset(asset: Asset, assetType: AssetType) {
        setState {
            when (assetType) {
                AssetType.Base -> {
                    copy(
                        baseAsset = asset,
                        quoteAsset = if (quoteAsset == asset) baseAsset else quoteAsset
                    )
                }

                AssetType.Quote -> {
                    copy(
                        baseAsset = if (baseAsset == asset) quoteAsset else baseAsset,
                        quoteAsset = asset
                    )
                }
            }
        }
    }
}