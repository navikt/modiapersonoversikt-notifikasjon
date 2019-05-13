package no.nav.modiapersonoversikt.model

import java.time.LocalDateTime
import java.util.*

data class NotifikasjonDTOIn(
        val tittel: String,
        val melding: String,
        val lenke: String?
)

data class Notifikasjon(
        val id: UUID,
        val tittel: String,
        val melding: String,
        val lenke: String?,
        val opprettetDato: LocalDateTime
) {
    constructor(id: UUID, notifikasjon: NotifikasjonDTOIn, opprettetDato: LocalDateTime): this(id, notifikasjon.tittel, notifikasjon.melding, notifikasjon.lenke, opprettetDato)
}

data class NotifikasjonDTOOut(
        val id: UUID,
        val tittel: String,
        val melding: String,
        val lenke: String?,
        val opprettetDato: LocalDateTime,
        val settTidligere: Boolean = false
) {
    constructor(notifikasjon: Notifikasjon, settTidligere: Boolean): this(notifikasjon.id, notifikasjon.tittel, notifikasjon.melding, notifikasjon.lenke, notifikasjon.opprettetDato, settTidligere)
}
