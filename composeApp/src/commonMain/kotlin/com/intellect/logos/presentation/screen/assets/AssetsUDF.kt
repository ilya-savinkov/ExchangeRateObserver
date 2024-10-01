package com.intellect.logos.presentation.screen.assets

import androidx.paging.PagingData
import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import kotlinx.coroutines.flow.Flow

object AssetsUDF {

    data class State(
        val assets: Flow<PagingData<Asset>>,
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

    data object Model : UDF.Model

    sealed interface Action : UDF.Action {
        data class TapAsset(val asset: Asset) : Action
        data class Search(val query: String) : Action
        data object Back : Action
    }


    sealed interface Event : UDF.Event
}