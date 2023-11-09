package com.apps.pochak.common;

import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.service.CommentService;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.oauth.OAuthService;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.service.PostService;
import com.apps.pochak.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class TestDataFactory {
    @Autowired
    OAuthService oAuthService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Test
    public void generateUserData() throws Exception {
        String userHandle = "5jizzi";
        String userKorName = "오지수";

        UserInfoRequest infoReq1 = UserInfoRequest.builder()
                .name(userKorName)
                .email(userHandle + "@apple.com")
                .handle(userHandle)
                .message(userKorName + " hi hi message test")
                .socialType("APPLE")
                .socialId("APPLE_ID")
                .profileImage(userKorName + " profile img url")
                .build();

        oAuthService.signup(infoReq1);
    }

    @Test
    public void generatePostData() throws Exception {

        String userHandle = "5jizzi";
        String userName = "jisoo";

        ArrayList<String> taggedList = new ArrayList<>();
        taggedList.add("dayeon");
        taggedList.add("goeun");


        PostUploadRequestDto reqDto = new PostUploadRequestDto();
        reqDto.setPostImageUrl(userName + " post url test");
        reqDto.setCaption("post published by " + userName);

        reqDto.setTaggedUserHandles(taggedList);

        postService.savePost(reqDto, userHandle);
    }

    @Test
    public void deletePostData() throws Exception {

        String postPK = "";
        String userHandle = "jisoo";

        postService.deletePost(postPK, userHandle);
    }

    @Test
    public void generateComment() throws Exception {
        String postPK = "";
        String loginUserHandle = "jisoo";
        String parentCommentSK = null;

        CommentUploadRequestDto requestDto = new CommentUploadRequestDto();
        requestDto.setContent(loginUserHandle + " comment test");
        requestDto.setParentCommentSK(parentCommentSK);

        commentService.commentUpload(postPK, requestDto, loginUserHandle);
    }
}
