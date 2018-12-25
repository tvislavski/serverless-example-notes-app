package me.tvislavski.serverlessexample.adapters.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@DynamoDBTable(tableName = "user")
@NoArgsConstructor
@Setter
@ToString
public class DynamoDbUser {

    private String email;
    private List<DynamoDbNote> notes;

    @DynamoDBHashKey
    public String getEmail() {
        return email;
    }

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute
    public List<DynamoDbNote> getNotes() {
        return notes;
    }
}
