package com.intellect.logos.presentation.screen.assets

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.common.presentation.ui.placeholder.SimplePlaceholderBox
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.assets
import exchangerateobserver.composeapp.generated.resources.back
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.AssetsContent(
    state: AssetsUDF.State,
    onAction: (AssetsUDF.Action) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier,
) {
//    val assets = state.assets.collectAsLazyPagingItems()
//
//    LazyColumn(
//        state = rememberLazyListState(),
//        modifier = modifier
//    ) {
//        // TODO add tracking current location
//        // TODO add favorite assets
//        // TODO add groups like "Currencies", "Crypto", "Stocks"
//
//        when (assets.loadState.refresh) {
//            LoadState.Loading -> loading()
//            is LoadState.Error -> error()
//            is LoadState.NotLoading -> if (assets.itemCount == 0) {
//                empty()
//            } else {
//                items(
//                    count = assets.itemCount,
//                    key = assets.itemKey { it.name }
//                ) {
//                    val asset = assets[it] ?: return@items
//
//                    AssetItem(
//                        asset = asset,
//                        searchQuery = state.searchState.query,
//                        onClick = { onAction(AssetsUDF.Action.TapAsset(asset)) },
//                        animatedVisibilityScope = animatedVisibilityScope,
//                        modifier = Modifier.animateItem()
//                    )
//                }
//            }
//        }
//    }
}

private fun LazyListScope.loading() {
    item {
        Column(
            modifier = Modifier
                .fillParentMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10) {
                SimplePlaceholderBox(
                    height = 100.dp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

private fun LazyListScope.error() {
    item {
        Box(
            modifier = Modifier.fillParentMaxSize()
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun LazyListScope.empty() {
    item {
        Box(
            modifier = Modifier.fillParentMaxSize()
        ) {
            Text(
                text = "Empty",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}