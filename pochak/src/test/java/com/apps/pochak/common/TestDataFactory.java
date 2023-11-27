package com.apps.pochak.common;

import com.apps.pochak.PochakApplication;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.service.CommentService;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.oauth.OAuthService;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.service.PostService;
import com.apps.pochak.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PochakApplication.class)
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
//        String userHandle = "goeun";
//        String userKorName = "하고은";
//
//        UserInfoRequest infoReq1 = UserInfoRequest.builder()
//                .name(userKorName)
//                .email(userHandle + "@apple.com")
//                .handle(userHandle)
//                .message(userKorName + " hi hi message test")
//                .socialType("APPLE")
//                .socialId(userHandle + "APPLE_ID")
//                .profileImage(getMockMultipartFile("APPS_LOGO", "PNG", "src/main/resources/static/APPS_LOGO.PNG"))
//                .build();
//
//        oAuthService.signup(infoReq1);
    }

    @Test
    public void generatePostData() throws Exception {

//        String userHandle = "jisoo";
//        String userName = "jisoo";
//
//        ArrayList<String> taggedList = new ArrayList<>();
//        taggedList.add("dayeon");
//        taggedList.add("goeun");
//
//
//        PostUploadRequestDto reqDto = new PostUploadRequestDto();
//        reqDto.setPostImage(getMockMultipartFile("APPS_LOGO", "PNG", "src/main/resources/static/APPS_LOGO.PNG"));
//        reqDto.setCaption("post published by " + userName);
//
//        reqDto.setTaggedUserHandles(taggedList);
//
//        postService.savePost(reqDto, userHandle);
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    public void deletePostData() throws Exception {

//        String postPK = "POST#eb472472-97ea-40ab-97e7-c5fdf57136a0";
//        String userHandle = "jisoo";
//
//        postService.deletePost(postPK, userHandle);
    }

    @Test
    public void generateComment() throws Exception {
//        String postPK = "POST#eb472472-97ea-40ab-97e7-c5fdf57136a0";
//        String loginUserHandle = "jisoo";
//        String parentCommentSK = null;
//
//        CommentUploadRequestDto requestDto = new CommentUploadRequestDto();
//        requestDto.setContent(loginUserHandle + " comment test");
//        requestDto.setParentCommentSK(parentCommentSK);
//
//        commentService.commentUpload(postPK, requestDto, loginUserHandle);
    }
}
