package com.intellect.logos.presentation.screen.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.back
import exchangerateobserver.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onAction(StateUDF.Action.Close)
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
        SettingsContent(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun SettingsContent(
    state: StateUDF.State,
    onAction: (StateUDF.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO Add theme switcher
    // TODO Add language switcher
}