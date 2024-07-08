package com.intellect.logos.presentation.screen.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.udf.UDF

object AssetsUDF {

    data class State(
        val assets: List<Asset> = emptyList(),
        val selectedAsset: Asset,
        val searchState: SearchState = SearchState(),
        val errorState: ErrorState? = null,
    ) : UDF.State {
        data class SearchState(
            val query: String = "",
        )

        data class ErrorState(
            val message: String,
        )
    }

    sealed interface Action : UDF.Action {
        data class TapAsset(val asset: Asset) : Action
        data class Search(val query: String) : Action
        data object Back : Action
    }


    sealed interface Event : UDF.Event
}