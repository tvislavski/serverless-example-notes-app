package me.tvislavski.serverlessexample.adapters.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.tvislavski.serverlessexample.model.Error;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public abstract class S3Adapter<T> {

    @Getter(AccessLevel.PROTECTED)
    private String bucket;
    @Getter(AccessLevel.PROTECTED)
    private AmazonS3 s3Client;

    public S3Adapter(String bucket) {
        this(bucket, AmazonS3ClientBuilder.defaultClient());
    }

    public S3Adapter(String bucket, AmazonS3 s3Client) {
        this.bucket = bucket;
        this.s3Client = s3Client;
    }

    public Either<Error, String> upload(T object, Option<String> subfolder) {
        return Try.of(() -> {
            String attachmentUUID = UUID.randomUUID().toString();
            String key = subfolder.map(s -> s.concat("/")).getOrElse("").concat(attachmentUUID);
            byte[] bytes = byteArrayProducer(object);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            s3Client.putObject(getBucket(), key, new ByteArrayInputStream(bytes), metadata);
            return key;
        }).toEither().mapLeft(throwable -> {
            log.error(throwable.toString());
            return new Error("S3 upload error");
        });
    }

    protected abstract byte[] byteArrayProducer(T object);

    public Either<Error, T> load(String arn){
        return Try.of(() -> {
            S3Object s3Object = s3Client.getObject(getBucket(), arn);
            return readFrom(s3Object.getObjectContent());
        }).toEither().mapLeft(throwable -> {
            log.error(throwable.toString());
            return new Error("S3 download error");
        }).flatMap(this::factoryMethod);
    }

    protected abstract Either<Error, T> factoryMethod(byte[] bytes);

    protected byte[] readFrom(S3ObjectInputStream s3Stream) throws IOException {
        int bufferSize = 1024;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bufferSize);
        int length;
        byte[] buffer = new byte[bufferSize];
        while ((length = s3Stream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        s3Stream.close();
        byte[] result = outputStream.toByteArray();
        outputStream.close();
        return result;
    }
}
