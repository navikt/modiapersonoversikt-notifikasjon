package no.nav.modiapersonoversikt.storage

import no.nav.modiapersonoversikt.model.Melding
import java.util.*

interface StorageProvider {
    fun getMeldinger(): List<Melding>

    fun getMelding(id: UUID): Melding

    fun putMelding(melding: Melding): UUID

    fun fjernMelding(id: UUID)
}
