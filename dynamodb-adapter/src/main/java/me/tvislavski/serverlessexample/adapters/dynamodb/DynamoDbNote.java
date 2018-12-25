package me.tvislavski.serverlessexample.adapters.dynamodb;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DynamoDbNote {

    private Date created;
    private String text;
    private List<String> attachmentsS3ARNs;
}
