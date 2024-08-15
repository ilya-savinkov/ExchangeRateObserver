package com.intellect.logos.presentation.screen.assets

import androidx.paging.PagingData
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.usecase.assets.GetAssetsUseCase
import com.intellect.logos.domain.usecase.assets.SetDefaultAssetUseCase
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Action
import com.intellect.logos.presentation.screen.assets.AssetsUDF.Event
import com.intellect.logos.presentation.screen.assets.AssetsUDF.State
import kotlinx.coroutines.flow.flowOf
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class AssetsViewModel(
    private val getAssetsUseCase: GetAssetsUseCase,
    private val setDefaultAssetUseCase: SetDefaultAssetUseCase,
    private val router: AssetsRouter,
    @InjectedParam private val assetType: Asset.Type,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        assets = flowOf(PagingData.empty())
    )
) {
    override suspend fun onInit() {
        setState {
            copy(
                assets = getAssetsUseCase()
            )
        }
    }

    override suspend fun reduce(action: Action) = when (action) {
        is Action.TapAsset -> tapAsset(action.asset)
        is Action.Search -> search(action.query)
        Action.Back -> router.close()
    }

    private suspend fun tapAsset(asset: Asset) {
        setDefaultAssetUseCase(
            asset = asset.name,
            type = assetType
        )

        router.close()
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
}