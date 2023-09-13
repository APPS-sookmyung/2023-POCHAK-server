package com.apps.pochak.user.dto;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.publish.domain.Publish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.apps.pochak.user.dto.UserProfileResDto.ProfilePostDto;

@NoArgsConstructor
@Data
public class UserPublishResDto {
    private List<ProfilePostDto> uploadPosts;
    private Map<String, String> exclusiveStartKey;

    public UserPublishResDto(List<Publish> publishes, Map<String, String> exclusiveStartKey) {
        this.uploadPosts = publishes.stream().map(
                publish -> new ProfilePostDto(publish)
        ).collect(Collectors.toList());
        this.exclusiveStartKey = exclusiveStartKey;
    }
}
