package no.nav.modiapersonoversikt.storage

import no.nav.modiapersonoversikt.model.Notifikasjon
import no.nav.modiapersonoversikt.model.NotifikasjonDTOIn
import no.nav.modiapersonoversikt.model.NotifikasjonDTOOut
import java.time.LocalDateTime
import java.util.*

class LocalStorageProvider : StorageProvider {
    private var meldinger: MutableMap<UUID, Notifikasjon> = mutableMapOf()
    private var sistLest: MutableMap<String, LocalDateTime> = mutableMapOf()

    override fun hentNotifikasjoner(ident: String): List<NotifikasjonDTOOut> {
        val sistLest = hentSistLest(ident)
        return meldinger
                .entries
                .map { NotifikasjonDTOOut(it.value, it.value.opprettetDato.isBefore(sistLest)) }
    }

    override fun hentNotifikasjon(ident: String, id: UUID): NotifikasjonDTOOut? {
        val hentSistLest = hentSistLest(ident)
        return meldinger[id]
                ?.let { NotifikasjonDTOOut(it, it.opprettetDato.isBefore(hentSistLest)) }
    }

    override fun opprettNotifikasjon(notifikasjon: NotifikasjonDTOIn): UUID {
        val id = UUID.randomUUID()
        meldinger[id] = Notifikasjon(id, notifikasjon, LocalDateTime.now())
        return id
    }

    override fun slettNotifikasjon(id: UUID) {
        meldinger.remove(id)
    }

    override fun oppdaterSistLest(ident: String) {
        sistLest[ident] = LocalDateTime.now()
    }

    private fun hentSistLest(ident: String): LocalDateTime =
            sistLest.getOrPut(ident, { MIN_DATE })
}
