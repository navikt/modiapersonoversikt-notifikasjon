package no.nav.modiapersonoversikt.model

import java.time.LocalDateTime
import java.util.*

data class Melding(
        val id: UUID,
        val tittel: String,
        val melding: String,
        val lenke: String?,
        val opprettetDato: LocalDateTime
)
