package no.nav.modiapersonoversikt.storage

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import no.nav.modiapersonoversikt.DATE_FORMATTER
import no.nav.modiapersonoversikt.configuration
import no.nav.modiapersonoversikt.model.Notifikasjon
import no.nav.modiapersonoversikt.model.NotifikasjonDTOIn
import no.nav.modiapersonoversikt.model.NotifikasjonDTOOut
import no.nav.modiapersonoversikt.timed
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.*

private const val NOTIFIKASJON_BUCKET_NAME = "modiapersonoversikt-notifikasjon-bucket"
private const val NOTIFIKASJON_KEY_NAME = "notifikasjoner"
private const val SIST_LEST_BUCKET_NAME = "odiapersonoversikt-notifikasjon-sist-lest-bucket"
val MIN_DATE = LocalDateTime.parse("1970-01-01T00:00:00.000", DATE_FORMATTER)

class S3StorageProvider : StorageProvider {
    private val s3: AmazonS3
    private val gson = GsonBuilder().setPrettyPrinting().create()

    init {
        val credentials = BasicAWSCredentials(configuration.s3AccessKey, configuration.s3SecretKey)
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(configuration.s3Url, configuration.s3Region))
                .enablePathStyleAccess()
                .withCredentials(AWSStaticCredentialsProvider(credentials)).build()

        lagS3BucketsHvisNodvendig(NOTIFIKASJON_BUCKET_NAME, SIST_LEST_BUCKET_NAME)
    }

    override fun hentNotifikasjoner(ident: String): List<NotifikasjonDTOOut> =
            timed("hent_notifikasjoner") {
                val sistLest = hentSistLest(ident)
                hentNotifikasjoner()
                        .map { NotifikasjonDTOOut(it, it.opprettetDato.isBefore(sistLest)) }
            }

    override fun hentNotifikasjon(ident: String, id: UUID): NotifikasjonDTOOut? =
            timed("hent_notifikasjon") {
                val sistLest = hentSistLest(ident)
                hentNotifikasjoner()
                        .find { it.id == id }
                        ?.let { NotifikasjonDTOOut(it, it.opprettetDato.isBefore(sistLest)) }
            }

    override fun opprettNotifikasjon(notifikasjon: NotifikasjonDTOIn): UUID =
            timed("opprett_notifikasjon") {
                val id = UUID.randomUUID()
                val notifikasjonTilLagring = Notifikasjon(id, notifikasjon, LocalDateTime.now())
                val notifikasjoner = hentNotifikasjoner().plus(notifikasjonTilLagring)

                lagreNotifikasjoner(notifikasjoner)
                id
            }

    override fun slettNotifikasjon(id: UUID) {
        timed("slett_notifikasjon") {
            val notifikasjoner = hentNotifikasjoner().filter { it.id == id }
            lagreNotifikasjoner(notifikasjoner)
        }
    }

    override fun oppdaterSistLest(ident: String) {
        timed("oppdater_sist_lest") {
            s3.putObject(SIST_LEST_BUCKET_NAME, ident, DATE_FORMATTER.format(LocalDateTime.now()))
        }
    }

    private fun hentSistLest(ident: String): LocalDateTime =
            timed("hent_sist_lest") {
                try {
                    val sistLest = s3.getObjectAsString(SIST_LEST_BUCKET_NAME, ident)

                    LocalDateTime.parse(sistLest, DATE_FORMATTER)
                } catch (e: Exception) {
                    MIN_DATE
                }
            }

    private fun lagreNotifikasjoner(notifikasjoner: List<Notifikasjon>) =
        timed("lagre_notifikasjoner") {
            s3.putObject(NOTIFIKASJON_BUCKET_NAME, NOTIFIKASJON_KEY_NAME, gson.toJson(notifikasjoner))
        }
    private fun hentNotifikasjoner(): List<Notifikasjon> =
            timed("hent_notifikasjoner") {
                try {
                    val notifikasjonerContent = s3.getObject(NOTIFIKASJON_BUCKET_NAME, NOTIFIKASJON_KEY_NAME)
                    gson.fromJson(InputStreamReader(notifikasjonerContent.objectContent), object : TypeToken<List<Notifikasjon>>() {}.type)
                } catch (e: Exception) {
                    emptyList()
                }
            }

    private fun lagS3BucketsHvisNodvendig(vararg buckets: String) {
        timed("lag_buckets_hvis_nodvendig") {
            val s3BucketNames = s3.listBuckets().map { it.name }
            val missingBuckets = buckets.filter { !s3BucketNames.contains(it) }

            missingBuckets
                    .forEach {
                        s3.createBucket(CreateBucketRequest(it).withCannedAcl(CannedAccessControlList.Private))
                    }
        }
    }
}
