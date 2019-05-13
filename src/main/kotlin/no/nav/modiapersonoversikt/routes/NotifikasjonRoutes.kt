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
        get("/{ident}") {
            call.parameters["ident"]
                    ?.let(provider::hentNotifikasjoner)
                    ?: call.respond(HttpStatusCode.BadRequest)
        }

        post {
            call.respond(provider.opprettNotifikasjon(call.receive()))
        }

        post("/{ident}/lest") {
            call.parameters["ident"]
                    ?.let(provider::oppdaterSistLest)
                    ?: call.respond(HttpStatusCode.BadRequest)
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: call.respond(HttpStatusCode.BadRequest)
            call.respond(provider.slettNotifikasjon(UUID.fromString(id as String)))
        }
    }
}
