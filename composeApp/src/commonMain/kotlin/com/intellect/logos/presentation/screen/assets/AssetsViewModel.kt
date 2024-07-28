package com.intellect.logos.presentation.screen.assets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.presentation.navigation.getObject
import com.intellect.logos.common.presentation.navigation.getString
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.usecase.assets.GetAssetsUseCase
import com.intellect.logos.domain.usecase.assets.SetDefaultAssetUseCase
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Action
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Event
import com.intellect.logos.presentation.screen.assets.AssetsUDF.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

class AssetsViewModel(
    private val getAssetsUseCase: GetAssetsUseCase,
    private val setDefaultAssetUseCase: SetDefaultAssetUseCase,
    private val assetsRouter: AssetsRouter,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        selectedAsset = savedStateHandle.getObject(SELECTED_ASSET_KEY)
    )
) {
    companion object {
        const val ROUTE = "assets"
        const val SELECTED_ASSET_KEY = "selectedAsset"
        const val ASSET_TYPE_KEY = "assetType"
    }

    init {
        subscribeSearch()
    }

    override suspend fun reduce(action: Action) = when (action) {
        is Action.TapAsset -> tapAsset(action.asset)
        is Action.Search -> search(action.query)
        Action.Back -> assetsRouter.close()
    }

    private suspend fun tapAsset(asset: Asset) {
        setDefaultAssetUseCase(
            asset = asset.currency.code,
            type = Asset.Type.valueOf(savedStateHandle.getString(ASSET_TYPE_KEY))
        )

        assetsRouter.close()
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
                // TODO Exclude selected asset from search results
                // TODO Сортировать по популярности и последним выбранным
                val assets = getAssetsUseCase(query)

                setState {
                    copy(
                        assets = assets
                    )
                }
            }.launchIn(viewModelScope)
    }
}