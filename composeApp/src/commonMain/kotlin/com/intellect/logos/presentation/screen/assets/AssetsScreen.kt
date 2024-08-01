package com.intellect.logos.presentation.screen.assets

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.intellect.logos.presentation.screen.assets.component.AssetItem
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.assets
import exchangerateobserver.composeapp.generated.resources.back
import exchangerateobserver.composeapp.generated.resources.clear
import exchangerateobserver.composeapp.generated.resources.search
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.AssetsScreen(
    viewModel: AssetsViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            // TODO hide when scroll
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.assets),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onAction(AssetsUDF.Action.Back)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        AssetsContent(
            state = state,
            onAction = viewModel::onAction,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
private fun SharedTransitionScope.AssetsContent(
    state: AssetsUDF.State,
    onAction: (AssetsUDF.Action) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier,
) {
    val assets = state.assets.collectAsLazyPagingItems()

    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
    ) {
        stickyHeader {
            // TODO search currency by photo
            // TODO Заменить на обычный TextField
            SearchBar(
                query = state.searchState.query,
                onQueryChange = {
                    onAction(AssetsUDF.Action.Search(it))
                },
                onSearch = {},
                active = false,
                onActiveChange = {},
                placeholder = {
                    Text(stringResource(Res.string.search))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(Res.string.search)
                    )
                },
                trailingIcon = {
                    if (state.searchState.query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onAction(AssetsUDF.Action.Search(""))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(Res.string.clear)
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

            }
        }

        // TODO add favorite assets
        // TODO add groups like "Currencies", "Crypto", "Stocks"
        items(
            count = assets.itemCount,
            key = assets.itemKey { it.currency.code }
        ) {
            val asset = assets[it] ?: return@items

            AssetItem(
                asset = asset,
                searchQuery = state.searchState.query,
                onClick = { onAction(AssetsUDF.Action.TapAsset(asset)) },
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.animateItem()
            )
        }
    }
}