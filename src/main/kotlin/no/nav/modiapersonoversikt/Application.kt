package no.nav.modiapersonoversikt

import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

private val log = LoggerFactory.getLogger("modiapersonoversikt-innstillinger.Application")
data class ApplicationState(var running: Boolean = true, var initialized: Boolean = false)
val configuration = Configuration()

fun main() {
    val applicationState = ApplicationState()
    val applicationServer = createHttpServer(applicationState)

    Runtime.getRuntime().addShutdownHook(Thread {
        log.info("Shutdown hook called, shutting down gracefully")
        applicationState.initialized = false
        applicationServer.stop(5, 5, TimeUnit.SECONDS)
    })

    applicationServer.start(wait = true)
}