package com.apps.pochak.post.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResDto {

    // Service 함수를 resdto에 가져와서 사용해도 괜찮은건지 의문 (다른 방법을 찾고싶다)

    PostService postService;



    //  이 부분 예외처리 어떻게 해야할지 고민 (없애면 왜안됨?)
    @Builder
    public PostResDto(Post post,PostService postService) throws BaseException {
        this.postService = postService;

        this.caption=post.getCaption();
        this.postImageUrl=post.getImgUrl();
        this.numOfHeart=post.getLikeUsers().size();
        this.heartList=post.getLikeUsers();
        // commentId 받아와서 String comment message 리스트로 저장
        try{
            // ** here
            List getparentComments = postService.getParentComments(post);
            this.parentComments = getparentComments;
            this.numOfParentComments=getparentComments.size();
        }
        catch (BaseException e){
            throw e;
        }


    }

    // 기본정보
    private String postImageUrl;
    private String caption;


    // 좋아요 갯수
    private int numOfHeart;
    private List heartList;

    // 부모댓글
    private List parentComments;
    private int numOfParentComments;




}

