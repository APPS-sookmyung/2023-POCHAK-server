package com.apps.pochak.comment.dto.response;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.global.PageInfo;
import com.apps.pochak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.comment.service.CommentService.DEFAULT_PAGING_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentCommentElement {
    private String profileImage;
    private String handle;
    private LocalDateTime createdDate;
    private String content;
    private PageInfo childCommentPageInfo;
    private List<CommentElement> childCommentList;

    public ParentCommentElement(
            final Comment parentComment
    ) {
        this(
                parentComment,
                PageRequest.of(0, DEFAULT_PAGING_SIZE)
        );
    }

    public ParentCommentElement(
            final Comment parentComment,
            final PageRequest pageRequest
    ) {
        final Member member = parentComment.getMember();
        this.profileImage = member.getProfileImage();
        this.handle = member.getHandle();
        this.createdDate = parentComment.getCreatedDate();
        this.content = parentComment.getContent();

        final List<Comment> childCommentList = parentComment.getChildCommentList();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), childCommentList.size());

        final List<CommentElement> commentElementList = childCommentList.stream()
                .map(
                        CommentElement::new
                )
                .collect(Collectors.toList());

        final PageImpl<CommentElement> commentElementPage = new PageImpl<>(
                commentElementList.subList(start, end),
                pageRequest,
                commentElementList.size()
        );

        this.childCommentPageInfo = new PageInfo(commentElementPage);
        this.childCommentList = commentElementPage.getContent();
    }
}
