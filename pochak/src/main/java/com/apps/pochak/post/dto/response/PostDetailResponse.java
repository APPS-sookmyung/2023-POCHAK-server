package com.apps.pochak.post.dto.response;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.response.CommentElement;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    private String ownerHandle;
    private String ownerProfileImage;
    private List<String> taggedMemberHandle;
    private Boolean isFollow;
    private String postImage;
    private Boolean isLike;
    private int likeCount;
    private String caption;
    private CommentElement recentComment;

    @Builder(builderMethodName = "of")
    public PostDetailResponse(
            final Post post,
            final List<Tag> tagList,
            final Boolean isFollow,
            final Boolean isLike,
            final int likeCount,
            final Comment recentComment
    ) {
        final Member owner = post.getOwner();
        this.ownerHandle = owner.getHandle();
        this.ownerProfileImage = owner.getProfileImage();
        this.taggedMemberHandle = tagList.stream().map(
                tag -> tag.getMember().getHandle()
        ).collect(Collectors.toList());
        this.isFollow = isFollow;
        this.postImage = post.getPostImage();
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.caption = post.getCaption();
        this.recentComment = CommentElement.from()
                .comment(recentComment)
                .build();
    }
}
