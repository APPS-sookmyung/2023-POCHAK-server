package com.apps.pochak.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.apps.pochak.common.ApiDocumentUtils.getDocumentRequest;
import static com.apps.pochak.common.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CommentControllerTest {
    @Value("${test.authorization.dayeon}")
    String authorization;

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
    @DisplayName("Get Comments API Document")
    void getComments() throws Exception {
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/api/v2/posts/{postId}/comments", 2)
                                .header("Authorization", authorization)
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(
                        document("get-comments",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description(
                                                        "Basic auth credentials  \n" +
                                                                ": 만약 아직 공개된 게시물이 아니라면 (댓글이 당연히 없으므로) 오류가 발생합니다."
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
                                        fieldWithPath("result.parentCommentPageInfo").type(OBJECT).description("부모 댓글 페이징 정보"),
                                        fieldWithPath("result.parentCommentPageInfo.lastPage").type(BOOLEAN)
                                                .description(
                                                        "부모 댓글 페이징 정보 \n" +
                                                                ": 현재 페이지가 마지막 페이지인지의 여부"
                                                ),
                                        fieldWithPath("result.parentCommentPageInfo.totalPages").type(NUMBER)
                                                .description(
                                                        "부모 댓글 페이징 정보 \n" +
                                                                ": 총 페이지 수"
                                                ),
                                        fieldWithPath("result.parentCommentPageInfo.totalElements").type(NUMBER)
                                                .description(
                                                        "부모 댓글 페이징 정보 \n" +
                                                                ": 총 부모 댓글의 수"
                                                ),
                                        fieldWithPath("result.parentCommentPageInfo.size").type(NUMBER)
                                                .description(
                                                        "부모 댓글 페이징 정보 \n" +
                                                                ": 페이징 사이즈"
                                                ),
                                        fieldWithPath("result.parentCommentList").type(ARRAY).description("부모 댓글 리스트"),
                                        fieldWithPath("result.parentCommentList[].commentId").type(NUMBER)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 댓글 아이디"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].profileImage").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 작성자 프로필 사진"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].handle").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 작성자 아이디"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].createdDate").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 댓글 작성 시간"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].content").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 댓글 내용"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentPageInfo").type(OBJECT)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentPageInfo.lastPage").type(BOOLEAN)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 현재 페이지가 마지막 페이지인지의 여부"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentPageInfo.totalPages").type(NUMBER)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 총 페이지 수"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentPageInfo.totalElements").type(NUMBER)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 총 자식 댓글의 수"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentPageInfo.size").type(NUMBER)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 페이징 사이즈"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList").type(ARRAY)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 리스트"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList[].commentId").type(NUMBER)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 자식 댓글 아이디"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList[].profileImage").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 작성자 프로필 사진"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList[].handle").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 작성자 아이디"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList[].createdDate").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 댓글 작성 시간"
                                                ).optional(),
                                        fieldWithPath("result.parentCommentList[].childCommentList[].content").type(STRING)
                                                .description(
                                                        "부모 댓글 리스트 \n" +
                                                                ": 자식 댓글 페이징 정보 \n" +
                                                                ": 댓글 내용"
                                                ).optional(),
                                        fieldWithPath("result.loginMemberProfileImage").type(STRING)
                                                .description(
                                                        "로그인한 멤버의 프로필 이미지"
                                                )
                                )

                        )
                );
    }
}