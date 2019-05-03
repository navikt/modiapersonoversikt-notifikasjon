package no.nav.modiapersonoversikt

import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.request.path
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import no.nav.modiapersonoversikt.routes.naisRoutes
import no.nav.modiapersonoversikt.storage.S3StorageProvider
import no.nav.modiapersonoversikt.storage.StorageProvider
import org.slf4j.event.Level

fun createHttpServer(applicationState: ApplicationState,
                     provider: StorageProvider = S3StorageProvider(),
                     port: Int = 7070): ApplicationEngine = embeddedServer(Netty, port) {

    install(StatusPages) {
        notFoundHandler()
        exceptionHandler()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(CallLogging) {
        level = Level.TRACE
        filter { call -> call.request.path().startsWith("/innstillinger") }
    }

    routing {
        naisRoutes(readinessCheck = { applicationState.initialized }, livenessCheck = { applicationState.running })

    }

    applicationState.initialized = true
}
