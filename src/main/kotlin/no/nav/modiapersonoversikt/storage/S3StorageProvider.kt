package no.nav.modiapersonoversikt.storage

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.auth.BasicAWSCredentials
import no.nav.modiapersonoversikt.configuration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.google.gson.GsonBuilder
import no.nav.modiapersonoversikt.model.Melding
import java.util.*

private const val BUCKET_NAME = "modiapersonoversikt-notifikasjon-bucket"

class S3StorageProvider : StorageProvider {

    private val s3: AmazonS3
    private val gson = GsonBuilder().setPrettyPrinting().create()

    init {
        val credentials = BasicAWSCredentials(configuration.s3AccessKey, configuration.s3SecretKey)
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(configuration.s3Url, configuration.s3Region))
                .enablePathStyleAccess()
                .withCredentials(AWSStaticCredentialsProvider(credentials)).build()
        createBucketIfMissing()
    }

    override fun getMeldinger(): List<Melding> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMelding(id: UUID): Melding {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putMelding(melding: Melding): UUID {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fjernMelding(id: UUID) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createBucketIfMissing() {
        val bucketList = s3.listBuckets().filter { b -> b.name == BUCKET_NAME }
        if (bucketList.isEmpty()) {
            s3.createBucket(CreateBucketRequest(BUCKET_NAME).withCannedAcl(CannedAccessControlList.Private))
        }
    }
}
