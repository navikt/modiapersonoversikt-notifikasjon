package no.nav.modiapersonoversikt.model

import java.time.LocalDateTime
import java.util.*

data class Melding(val id: UUID, val melding: String, val opprettetDato: LocalDateTime)
