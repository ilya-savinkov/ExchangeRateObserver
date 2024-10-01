package com.intellect.logos

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIViewController


fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        // TODO Send all errors to analytics
        Napier.base(DebugAntilog())
        initKoin()
    }
) {
    App()
}