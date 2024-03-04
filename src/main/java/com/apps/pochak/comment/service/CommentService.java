package com.apps.pochak.comment.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.repository.AlarmRepository;
import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.repository.CommentRepository;
import com.apps.pochak.comment.dto.request.CommentUploadRequest;
import com.apps.pochak.comment.dto.response.CommentElements;
import com.apps.pochak.comment.dto.response.ParentCommentElement;
import com.apps.pochak.global.api_payload.exception.GeneralException;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.global.api_payload.code.status.ErrorStatus.INVALID_COMMENT_ID;
import static com.apps.pochak.global.api_payload.code.status.ErrorStatus.INVALID_POST_ID;
import static com.apps.pochak.global.converter.PageableToPageRequestConverter.toPageRequest;

@Service
@RequiredArgsConstructor
public class CommentService {
    public static final int DEFAULT_PAGING_SIZE = 30;

    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;

    private final JwtService jwtService;

    @Transactional
    public CommentElements getComments(
            final Long postId,
            final Pageable pageable
    ) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findPublicPostById(postId);
        final Page<Comment> commentList = commentRepository.findParentCommentByPost(post, pageable);
        return new CommentElements(loginMember, commentList);
    }

    @Transactional
    public ParentCommentElement getChildCommentsByParentCommentId(
            final Long postId,
            final Long parentCommentId,
            final Pageable pageable
    ) {
        final Comment comment = commentRepository.findParentCommentById(parentCommentId)
                .orElseThrow(() -> new GeneralException(INVALID_POST_ID));
        final Post post = postRepository.findPublicPostById(postId);
        return new ParentCommentElement(comment, toPageRequest(pageable));
    }

    @Transactional
    public void saveComment(Long postId, CommentUploadRequest request) {
        final Member member = jwtService.getLoginMember();
        final Post post = postRepository.findPublicPostById(postId);

        final Comment comment;
        if (isChildComment(request)) {
            comment = saveChildComment(
                    request,
                    member,
                    post
            );
        } else {
            comment = saveParentComment(
                    request,
                    member,
                    post
            );
        }

        sendPostOwnerCommentAlarm(comment, post.getOwner());
        sendTaggedPostCommentAlarm(comment);
    }

    private Boolean isChildComment(CommentUploadRequest request) {
        return request.getParentCommentId() == null;
    }

    private Comment saveChildComment(
            final CommentUploadRequest request,
            final Member member,
            final Post post
    ) {
        final Comment comment = request.toEntity(member, post);
        return commentRepository.save(comment);
    }

    private Comment saveParentComment(
            final CommentUploadRequest request,
            final Member member,
            final Post post
    ) {
        final Comment parentComment = commentRepository
                .findParentCommentById(request.getParentCommentId())
                .orElseThrow(() -> new GeneralException(INVALID_COMMENT_ID));
        final Comment comment = commentRepository.save(
                request.toEntity(
                        member,
                        post,
                        parentComment)
        );

        sendCommentReplyAlarm(comment, parentComment.getMember());

        return comment;
    }

    private void sendPostOwnerCommentAlarm(Comment comment, Member receiver) {
        final Alarm alarm = Alarm.getPostOwnerCommentAlarm(
                comment,
                receiver
        );
        alarmRepository.save(alarm);
    }

    private void sendTaggedPostCommentAlarm(Comment comment) {
        final List<Tag> tagList = tagRepository.findTagsByPost(comment.getPost());
        final List<Alarm> alarmList = tagList.stream().map(
                tag -> Alarm.getTaggedPostCommentAlarm(comment, tag.getMember())
        ).collect(Collectors.toList());

        alarmRepository.saveAll(alarmList);
    }

    private void sendCommentReplyAlarm(Comment comment, Member receiver) {
        final Alarm commentReplyAlarm = Alarm.getCommentReplyAlarm(
                comment,
                receiver
        );

        alarmRepository.save(commentReplyAlarm);
    }
}
