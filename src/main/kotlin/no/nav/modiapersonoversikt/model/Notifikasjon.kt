package no.nav.modiapersonoversikt.model

import java.time.LocalDateTime
import java.util.*

data class NotifikasjonDTOIn(
        val tittel: String,
        val melding: String,
        val lenke: String?
)

data class Notifikasjon(
        val tittel: String,
        val melding: String,
        val lenke: String?,
        val opprettetDato: LocalDateTime
) {
    constructor(notifikasjon: NotifikasjonDTOIn, opprettetDato: LocalDateTime): this(notifikasjon.tittel, notifikasjon.melding, notifikasjon.lenke, opprettetDato)
}

data class NotifikasjonDTOOut(
        val id: UUID,
        val tittel: String,
        val melding: String,
        val lenke: String?,
        val opprettetDato: LocalDateTime,
        val settTidligere: Boolean = false
) {
    constructor(id: UUID, notifikasjon: Notifikasjon): this(id, notifikasjon.tittel, notifikasjon.melding, notifikasjon.lenke, notifikasjon.opprettetDato)
}
