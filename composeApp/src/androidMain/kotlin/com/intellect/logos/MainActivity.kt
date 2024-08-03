package com.intellect.logos

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import com.intellect.logos.presentation.theme.backgroundDark
import com.intellect.logos.presentation.theme.backgroundLight

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            // TODO Неправильный цвет на NavigationBar
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