package com.intellect.logos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.intellect.logos.presentation.theme.backgroundDark
import com.intellect.logos.presentation.theme.backgroundLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = backgroundLight.toArgb(),
                darkScrim = backgroundDark.toArgb()
            )
        )

        setContent {
            App()
        }
    }
}