package com.intellect.logos.presentation.screen.assets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.usecase.assets.GetAssetsUseCase
import com.intellect.logos.presentation.extension.getValue
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Action
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Event
import com.intellect.logos.presentation.screen.assets.AssetsUDF.State
import com.intellect.logos.presentation.udf.BaseViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

class AssetsViewModel(
    private val getAssetsUseCase: GetAssetsUseCase,
    private val assetsRouter: AssetsRouter,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        selectedAsset = savedStateHandle.getValue(SELECTED_ASSET_KEY)
    )
) {
    companion object {
        const val ROUTE = "assets"
        const val SELECTED_ASSET_KEY = "selectedAsset"
    }

    init {
        subscribeSearch()
    }

    override suspend fun reduce(action: Action) = when (action) {
        is Action.TapAsset -> tapAsset(action.asset)
        is Action.Search -> search(action.query)
        Action.Back -> assetsRouter.close()
    }

    private fun tapAsset(asset: Asset) {
        assetsRouter.closeWithResult(asset)
    }

    private fun search(query: String) {
        setState {
            copy(
                searchState = searchState.copy(
                    query = query
                )
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun subscribeSearch() {
        state
            .map { it.searchState.query }
            .distinctUntilChanged()
            .debounce(500.milliseconds)
            .onEach { query ->
                updateAssets(query) // TODO add pagination
            }.launchIn(viewModelScope)
    }

    private suspend fun updateAssets(query: String) {
        getAssetsUseCase(query).onSuccess { assets ->
            setState {
                copy(
                    assets = assets
                )
            }
        }.onFailure {
            Napier.e(it.message.orEmpty(), it)

            setState {
                copy(
                    errorState = State.ErrorState(
                        message = it.message.orEmpty()
                    )
                )
            }
        }
    }
}