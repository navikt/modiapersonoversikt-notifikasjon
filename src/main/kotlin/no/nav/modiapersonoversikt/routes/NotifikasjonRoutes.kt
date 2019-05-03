package no.nav.modiapersonoversikt.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import no.nav.modiapersonoversikt.storage.StorageProvider
import java.util.*

fun Routing.notifikasjonRoutes(provider: StorageProvider) {
    route("/notifikasjoner") {
        get {
            call.respond(provider.getMeldinger())
        }

        post {
            provider.putMelding(call.receive())
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: call.respond(HttpStatusCode.BadRequest)
            provider.fjernMelding(UUID.fromString(id as String))
        }
    }
}
