package com.intellect.logos

import android.content.Context
import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

actual class KoinInitializer(private val context: Context) {

    actual val viewModelModule: Module = module {
        viewModelOf(::ExchangeViewModel)
        viewModelOf(::AssetsViewModel)
    }

    actual fun init() {
        startKoin {
            androidContext(context)
            androidLogger()

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
    single { getDatabaseBuilder(androidContext()) }
    single { createDataStore(androidContext()) }
}