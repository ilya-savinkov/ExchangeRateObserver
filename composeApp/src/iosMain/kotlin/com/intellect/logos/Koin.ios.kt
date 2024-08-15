package com.intellect.logos

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

actual val platformModule: Module = module {
    single { getDatabaseBuilder() }
    single { createDataStore() }
}

fun initKoin() {
    startKoin {
        modules(
            platformModule,
            apiModule,
            dbModule,
            AppModule().module
        )
    }
}