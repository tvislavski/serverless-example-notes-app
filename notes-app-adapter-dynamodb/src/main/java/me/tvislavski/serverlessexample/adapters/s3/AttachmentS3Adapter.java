package me.tvislavski.serverlessexample.adapters.s3;

import com.amazonaws.services.s3.AmazonS3;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import me.tvislavski.serverlessexample.model.Error;
import me.tvislavski.serverlessexample.model.user.Attachment;

@Slf4j
public class AttachmentS3Adapter extends S3Adapter<Attachment> {

    public AttachmentS3Adapter(String bucket) {
        super(bucket);
    }

    public AttachmentS3Adapter(String bucket, AmazonS3 s3Client) {
        super(bucket, s3Client);
    }

    @Override
    protected byte[] byteArrayProducer(Attachment object) {
        return object.getData();
    }

    @Override
    protected Either<Error, Attachment> factoryMethod(byte[] bytes) {
        return Attachment.from(bytes);
    }
}
