package com.apps.pochak.post.controller;

import com.apps.pochak.post.dto.request.PostUploadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.util.ArrayList;

import static com.apps.pochak.common.ApiDocumentUtils.getDocumentRequest;
import static com.apps.pochak.common.ApiDocumentUtils.getDocumentResponse;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PostControllerTest {

    @Value("${test.authorization.dayeon}")
    String authorization1;

    @Value("${test.authorization.goeun}")
    String authorization3;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Post Upload API Document")
    void uploadPostTest() throws Exception {
        final String fileName = "APPS_LOGO";
        final String fileType = "PNG";

        final FileInputStream fileInputStream
                = new FileInputStream("src/test/resources/static/" + fileName + "." + fileType);
        final MockMultipartFile postImage = new MockMultipartFile(
                "postImage",
                fileName + "." + fileType,
                "multipart/form-data",
                fileInputStream
        );

        final String caption = "안녕하세요. 게시물 업로드를 테스트해보겠습니다.";
        final ArrayList<String> taggedMemberHandles = new ArrayList<>();
        taggedMemberHandles.add("_skf__11");
        taggedMemberHandles.add("habongee");

        final PostUploadRequest postUploadRequest = new PostUploadRequest(caption, taggedMemberHandles);

        final String content = objectMapper.writeValueAsString(postUploadRequest);
        final MockMultipartFile request
                = new MockMultipartFile(
                "request",
                "request",
                "application/json",
                content.getBytes(UTF_8)
        );

        this.mockMvc.perform(
                        multipart("/api/v2/posts")
                                .file(postImage)
                                .file(request)
                                .header("Authorization", authorization1)
                ).andExpect(status().isOk())
                .andDo(
                        document("upload-post",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic auth credentials")
                                ),
                                requestParts(
                                        partWithName("postImage").description("업로드 할 게시물 사진 파일 : 빈 파일 전달 시 에러 발생"),
                                        partWithName("request").description("게시물 업로드 DTO")
                                ),
                                requestPartFields(
                                        "request",
                                        fieldWithPath("caption").type(STRING)
                                                .description(
                                                        "`request.content` 업로드 할 게시물 내용 \n" +
                                                                ": null 또는 empty 문자열 전달도 가능합니다."
                                                ),
                                        fieldWithPath("taggedMemberHandleList").type(ARRAY)
                                                .description(
                                                        "`request.taggedMemberHandleList` 태그된 멤버 아이디 리스트 \n" +
                                                                ": 1개 이상의 아이디를 전달해야 합니다."
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )

                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("Get Post Detail API Document")
    void getPostDetailTest() throws Exception {
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/api/v2/posts/{postId}", 2)
                                .header("Authorization", authorization1)
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        document("get-detail-post",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description(
                                                        "Basic auth credentials  \n" +
                                                                ": 만약 아직 수락된 게시물이 아니라면 게시자와 태그된 사람만 접근 가능합니다."
                                                )
                                ),
                                pathParameters(
                                        parameterWithName("postId").description("게시물 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.ownerHandle").type(STRING).description("게시자 아이디 (handle)"),
                                        fieldWithPath("result.ownerProfileImage").type(STRING).description("게시자 프로필 이미지"),
                                        fieldWithPath("result.taggedMemberHandle").type(ARRAY).description("태그된 유저들의 아이디"),
                                        fieldWithPath("result.isFollow").type(BOOLEAN)
                                                .description(
                                                        "현재 로그인한 유저가 게시자를 팔로우하고 있는지 여부 \n" +
                                                                ": 만약 로그인한 유저가 게시자라면 null로 전달됨."
                                                ),
                                        fieldWithPath("result.postImage").type(STRING).description("게시물 이미지 URL"),
                                        fieldWithPath("result.isLike").type(BOOLEAN)
                                                .description(
                                                        "현재 로그인한 유저가 해당 게시물의 좋아요를 눌렀는지 여부"
                                                ),
                                        fieldWithPath("result.likeCount").type(NUMBER).description("게시물의 좋아요 개수"),
                                        fieldWithPath("result.caption").type(STRING).description("게시물의 caption"),
                                        fieldWithPath("result.recentComment").type(OBJECT)
                                                .description(
                                                        "게시물의 가장 최근 댓글 : 댓글이 없는 경우 NULL이 전달됨."
                                                ),
                                        fieldWithPath("result.recentComment.profileImage").type(STRING)
                                                .description(
                                                        "게시물의 가장 최근 댓글 : 댓글 게시자의 프로필 이미지"
                                                ).optional(),
                                        fieldWithPath("result.recentComment.handle").type(STRING)
                                                .description(
                                                        "게시물의 가장 최근 댓글 : 댓글 게시자의 아이디 (handle)"
                                                ).optional(),
                                        fieldWithPath("result.recentComment.createdDate").type(STRING)
                                                .description(
                                                        "게시물의 가장 최근 댓글 : 댓글 게시 시간"
                                                ).optional(),
                                        fieldWithPath("result.recentComment.content").type(STRING)
                                                .description(
                                                        "게시물의 가장 최근 댓글 : 댓글 내용"
                                                ).optional()
                                )

                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("Delete Post API Document")
    void deletePostTest() throws Exception {
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .delete("/api/v2/posts/{postId}", 2)
                                .header("Authorization", authorization3)
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        document("delete-post",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description(
                                                        "Basic auth credentials  \n" +
                                                                ": 만약 자신의 게시물이 아니라면 권한 에러가 발생합니다."
                                                )
                                ),
                                pathParameters(
                                        parameterWithName("postId").description("게시물 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        )
                );
    }
}