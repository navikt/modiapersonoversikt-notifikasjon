package no.nav.modiapersonoversikt.storage

import no.nav.modiapersonoversikt.model.NotifikasjonDTOIn
import no.nav.modiapersonoversikt.model.NotifikasjonDTOOut
import java.util.*

interface StorageProvider {
    fun hentNotifikasjoner(ident: String): List<NotifikasjonDTOOut>

    fun hentNotifikasjon(id: UUID): NotifikasjonDTOOut?

    fun opprettNotifikasjon(notifikasjon: NotifikasjonDTOIn): UUID

    fun slettNotifikasjon(id: UUID)

    fun oppdaterSistLest(ident: String)
}
