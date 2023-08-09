package com.apps.pochak.publish.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.apps.pochak.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class Publish extends BaseEntity {
    @Id
    private PublishId publishId;
    private String userHandle;
    private String uploadedDate;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String postPK;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String postImg;

    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getUserHandle() {
        return publishId != null ? publishId.getUserHandle() : null;
    }

    public void setUserHandle(String userHandle) {
        if (publishId == null) {
            publishId = new PublishId();
        }
        publishId.setUserHandle(userHandle);
    }

    @DynamoDBRangeKey(attributeName = "SortKey")
    public String getUploadedDate() {
        return publishId != null ? publishId.getUploadedDate() : null;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        if (publishId == null) {
            publishId = new PublishId();
        }
        publishId.setUploadedDate("PUBLISH#" + uploadedDate.toString());
    }
}
