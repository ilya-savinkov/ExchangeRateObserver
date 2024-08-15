package com.intellect.logos

import ServerConstant
import com.intellect.logos.data.database.configureDatabase
import com.intellect.logos.presentation.plugins.configureContentNegotiation
import com.intellect.logos.presentation.plugins.configureKoin
import com.intellect.logos.presentation.plugins.configureRouting
import com.intellect.logos.presentation.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    configureDatabase()

    embeddedServer(
        factory = Netty,
        host = ServerConstant.HOST,
        port = ServerConstant.PORT,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureContentNegotiation()
    configureStatusPages()
    configureRouting()
}
