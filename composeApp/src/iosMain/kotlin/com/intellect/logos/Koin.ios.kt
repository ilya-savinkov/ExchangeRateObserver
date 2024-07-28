package com.intellect.logos

import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual class KoinInitializer {

    actual val viewModelModule: Module = module {
        singleOf(::ExchangeViewModel)
        singleOf(::AssetsViewModel)
    }

    actual fun init() {
        startKoin {
            modules(
                platformModule,
                dbModule,
                apiModule,
                exchangeRateModule,
                assetsModule,
                viewModelModule
            )
        }
    }
}

actual val platformModule: Module = module {
    factory { getDatabaseBuilder() }
}