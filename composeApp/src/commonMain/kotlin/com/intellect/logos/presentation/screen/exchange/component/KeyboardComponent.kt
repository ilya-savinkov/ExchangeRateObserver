package com.intellect.logos.presentation.screen.exchange.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.intellect.logos.presentation.screen.exchange.model.Key
import com.intellect.logos.presentation.theme.CustomIcon
import com.intellect.logos.presentation.theme.customicon.Backspace

@Composable
fun KeyboardComponent(
    onKeyClick: (Key) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val keyModifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clip(CircleShape)

        KeysRow {
            NumberKey(key = 1, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 2, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 3, onClick = onKeyClick, modifier = keyModifier)
        }

        KeysRow {
            NumberKey(key = 4, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 5, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 6, onClick = onKeyClick, modifier = keyModifier)
        }

        KeysRow {
            NumberKey(key = 7, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 8, onClick = onKeyClick, modifier = keyModifier)
            NumberKey(key = 9, onClick = onKeyClick, modifier = keyModifier)
        }

        KeysRow {
            CharKey(key = '.', onClick = { onKeyClick(Key.Dot) }, modifier = keyModifier)
            NumberKey(key = 0, onClick = onKeyClick, modifier = keyModifier)

            IconKey(
                imageVector = CustomIcon.Backspace,
                contentDescription = "Backspace",
                onClick = { onKeyClick(Key.Backspace) },
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
    onClick: (Key.Number) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.clickable { onClick(Key.Number(key)) }) {
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