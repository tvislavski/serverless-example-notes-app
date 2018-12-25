package me.tvislavski.serverlessexample.adapters.dynamodb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import io.vavr.control.Either;
import me.tvislavski.serverlessexample.adapters.s3.AttachmentS3Adapter;
import me.tvislavski.serverlessexample.model.Error;
import me.tvislavski.serverlessexample.model.user.Email;
import me.tvislavski.serverlessexample.model.user.User;
import me.tvislavski.serverlessexample.ports.repository.UserNotFound;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class DynamoDbUserRepositoryTest {

    private static AmazonDynamoDB dynamoDB;
    private static DynamoDBMapper dbMapper;
    @Mock
    private AttachmentS3Adapter s3;
    private Email validEmail = Email.from("test@example.com").get();

    private DynamoDbUserRepository repository;

    @BeforeClass
    public static void beforeClass() {
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(TestUtils.getCredentialsProvider())
                .withEndpointConfiguration(TestUtils.getEndpointConfiguration())
                .build();
        dbMapper = new DynamoDBMapper(dynamoDB);
        CreateTableRequest createTableRequest = dbMapper.generateCreateTableRequest(DynamoDbUser.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        dynamoDB.createTable(createTableRequest);
    }

    @AfterClass
    public static void afterClass() {
        dynamoDB.deleteTable("user");
    }

    @Before
    public void setUp() {
        repository = new DynamoDbUserRepository(dbMapper, s3);
    }

    @Test
    public void getUserByWithNullEmailShouldFail() {
        assertThat(repository.getUserBy(null)).isLeft();
    }

    @Test
    public void getUserByWithUnsavedUserEmailShouldFail() {
        Either<Error, User> user = repository.getUserBy(Email.from(UUID.randomUUID().toString() + "@example.com").get());
        assertThat(user).containsLeftInstanceOf(UserNotFound.class);
    }

    @Test
    public void saveUserShouldSucceed() {
        Either<Error, User> user = repository.save(User.from(validEmail).get());
        assertThat(user).isRight();
    }

    private static class TestUtils extends cloud.localstack.TestUtils {

        static AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {
            // LocalStack DynamoDB instance
            return cloud.localstack.TestUtils.getEndpointConfiguration("http://localhost:4569/");
        }

    }
}
