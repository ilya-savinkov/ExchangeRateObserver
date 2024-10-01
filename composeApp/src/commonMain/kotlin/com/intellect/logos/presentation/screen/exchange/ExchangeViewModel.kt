package com.intellect.logos.presentation.screen.exchange

import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.empty
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.model.exchange.default
import com.intellect.logos.domain.model.exchange.empty
import com.intellect.logos.domain.usecase.assets.GetDefaultAssetsFlowUseCase
import com.intellect.logos.domain.usecase.assets.SwapAssetsUseCase
import com.intellect.logos.domain.usecase.rates.GetRateUseCase
import com.intellect.logos.domain.usecase.volume.GetVolumeFlowUseCase
import com.intellect.logos.domain.usecase.volume.UpdateVolumeUseCase
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModelUDF.Action
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModelUDF.Event
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModelUDF.Model
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModelUDF.State
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeComponent
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardComponent
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class ExchangeViewModel(
    private val getDefaultAssetsFlowUseCase: GetDefaultAssetsFlowUseCase,
    private val getVolumeFlowUseCase: GetVolumeFlowUseCase,
    private val router: ExchangeRouter,
    swapAssetsUseCase: SwapAssetsUseCase,
    getRateUseCase: GetRateUseCase,
    updateVolumeUseCase: UpdateVolumeUseCase
) : BaseViewModel<State, Action, Event, Model>(
    model = Model(
        isKeyboardEnabled = MutableStateFlow(false),
        isLoadingAssets = MutableStateFlow(true),
        baseAsset = MutableStateFlow(Asset.empty()),
        quoteAsset = MutableStateFlow(Asset.empty()),
        volume = MutableStateFlow(Volume.default()),
        convertedVolume = MutableStateFlow(""),
        isLoadingRate = MutableStateFlow(true),
        rate = MutableStateFlow(Rate.empty())
    )
) {
    val exchangeComponent: ExchangeComponent = ExchangeComponent(
        getRateUseCase = getRateUseCase,
        swapAssetsUseCase = swapAssetsUseCase,
        router = router,
        model = model,
        coroutineScope = viewModelScope
    )

    val keyboardComponent: KeyboardComponent = KeyboardComponent(
        updateVolumeUseCase = updateVolumeUseCase,
        model = model,
        coroutineScope = viewModelScope
    )

    init {
        viewModelScope.launch {
            combine(
                getDefaultAssetsFlowUseCase(),
                getVolumeFlowUseCase()
            ) { defaultAssets, volume ->
                defaultAssets.onSuccess { (baseAsset, quoteAsset) ->
                    model.isLoadingAssets.update { false }
                    model.isKeyboardEnabled.update { true }
                    model.volume.update { volume }
                    model.baseAsset.update { baseAsset }
                    model.quoteAsset.update { quoteAsset }
                }.onFailure {
                    Napier.e(it) { "ExchangeRateViewModel" }
                    sendEvent(Event.FailedToLoadAssets)
                }
            }.launchIn(viewModelScope)
        }
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            Action.OpenSettings -> router.openSettings()
        }
    }
}