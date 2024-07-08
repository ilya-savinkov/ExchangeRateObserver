package com.intellect.logos

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIViewController


fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        Napier.base(DebugAntilog())
        KoinInitializer().init()
    }
) {
    App()
}