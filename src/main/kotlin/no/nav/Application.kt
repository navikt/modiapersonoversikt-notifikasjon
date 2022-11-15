package no.nav

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    //configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    routing {
        route("internal") {
            get("isAlive") {
                call.respondText("Alive")
            }
            get("isReady") {
                call.respondText("Ready")
            }
        }
    }
}
