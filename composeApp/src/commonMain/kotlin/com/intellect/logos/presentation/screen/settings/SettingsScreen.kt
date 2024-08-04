package com.intellect.logos.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme
import exchangerateobserver.composeapp.generated.resources.Res
import exchangerateobserver.composeapp.generated.resources.back
import exchangerateobserver.composeapp.generated.resources.language
import exchangerateobserver.composeapp.generated.resources.settings
import exchangerateobserver.composeapp.generated.resources.theme
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ThemeOption(
            theme = state.theme.name,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(16.dp))

        LanguageOption(
            language = state.language.description,
            onAction = onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeOption(
    theme: String,
    onAction: (StateUDF.Action) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        TextField(
            label = { Text(text = stringResource(Res.string.theme)) },
            value = theme,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            Theme.entries.forEach { theme ->
                DropdownMenuItem(
                    text = {
                        Text(text = theme.name)
                    },
                    onClick = {
                        isExpanded = false
                        onAction(StateUDF.Action.ChangeTheme(theme))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageOption(
    language: String,
    onAction: (StateUDF.Action) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        TextField(
            label = { Text(text = stringResource(Res.string.language)) },
            value = language,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            Language.entries.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(text = language.description)
                    },
                    onClick = {
                        isExpanded = false
                        onAction(StateUDF.Action.ChangeLanguage(language))
                    }
                )
            }
        }
    }
}