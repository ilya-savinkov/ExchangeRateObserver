package com.intellect.logos.presentation.screen.exchange.component.keyboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.exchange.Key

@Composable
fun KeyboardComposable(
    component: KeyboardComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val keyModifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clip(CircleShape)

        KeysRow {
            NumberKey(key = 1, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 2, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 3, onAction = component::onAction, modifier = keyModifier)
        }

        KeysRow {
            NumberKey(key = 4, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 5, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 6, onAction = component::onAction, modifier = keyModifier)
        }

        KeysRow {
            NumberKey(key = 7, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 8, onAction = component::onAction, modifier = keyModifier)
            NumberKey(key = 9, onAction = component::onAction, modifier = keyModifier)
        }

        KeysRow {
            CharKey(
                key = '.',
                onClick = { component.onAction(KeyboardUDF.Action.TapKey(Key.Dot)) },
                modifier = keyModifier
            )

            NumberKey(key = 0, onAction = component::onAction, modifier = keyModifier)

            IconKey(
                imageVector = Icons.Default.Delete,
                contentDescription = "Backspace",
                onClick = { component.onAction(KeyboardUDF.Action.TapKey(Key.Backspace)) },
                modifier = keyModifier
            )
        }
    }
}

@Composable
private fun KeysRow(rowScope: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
    ) {
        rowScope()
    }
}

@Composable
private fun NumberKey(
    key: Int,
    onAction: (KeyboardUDF.Action) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.clickable { onAction(KeyboardUDF.Action.TapKey(Key.Number(key))) }) {
        Text(
            text = key.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CharKey(
    key: Char,
    onClick: (Char) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.clickable { onClick(key) }) {
        Text(
            text = key.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun IconKey(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}