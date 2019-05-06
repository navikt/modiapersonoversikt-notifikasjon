package no.nav.modiapersonoversikt.storage

import no.nav.modiapersonoversikt.model.Melding
import java.time.LocalDateTime
import java.util.*

class LocalStorageProvider : StorageProvider {

    override fun getMeldinger(): List<Melding> {
        return emptyList()
    }

    override fun getMelding(id: UUID): Melding {
        return Melding(UUID.randomUUID(), "Tittel", "TEST", "lenke", LocalDateTime.now())
    }

    override fun putMelding(melding: Melding): UUID {
        return UUID.randomUUID()
    }

    override fun fjernMelding(id: UUID) {

    }


}
