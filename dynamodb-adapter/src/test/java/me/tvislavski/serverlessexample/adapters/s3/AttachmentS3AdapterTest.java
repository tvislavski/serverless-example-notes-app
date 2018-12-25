package me.tvislavski.serverlessexample.adapters.s3;

import cloud.localstack.TestUtils;
import io.vavr.control.Option;
import me.tvislavski.serverlessexample.domain.user.Attachment;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class AttachmentS3AdapterTest {

    @Test
    public void uploadAndDownloadWithLocalS3InstanceShouldSucceed() throws IOException {
        var s3 = TestUtils.getClientS3();
        s3.createBucket("bucket");
        var attachmentS3Adapter = new AttachmentS3Adapter("bucket", s3);

        var input = getClass().getClassLoader().getResourceAsStream("smile.png");
        var uploadResult = attachmentS3Adapter.upload(Attachment.from(input.readAllBytes()).get(), Option.some("prefix"));
        assertThat(uploadResult).isRight();
        input.close();

        var downloadResult = attachmentS3Adapter.load(uploadResult.get());
        assertThat(downloadResult).isRight();
        input = getClass().getClassLoader().getResourceAsStream("smile.png");
        Assert.assertArrayEquals(input.readAllBytes(), downloadResult.get().getData());
        input.close();
    }
}
