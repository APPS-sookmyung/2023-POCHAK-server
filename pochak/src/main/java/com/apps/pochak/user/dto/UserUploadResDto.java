package com.apps.pochak.user.dto;

import com.apps.pochak.publish.domain.Publish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.user.dto.UserProfileResDto.ProfilePostDto;

@NoArgsConstructor
@Data
public class UserUploadResDto {
    private List<ProfilePostDto> uploadPosts;

    public UserUploadResDto(List<Publish> publishes) {
        this.uploadPosts = publishes.stream().map(
                publish -> new ProfilePostDto(publish)
        ).collect(Collectors.toList());
    }
}
