package com.apps.pochak.post.controller;

import com.apps.pochak.post.dto.request.PostUploadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.util.ArrayList;

import static com.apps.pochak.common.ApiDocumentUtils.getDocumentRequest;
import static com.apps.pochak.common.ApiDocumentUtils.getDocumentResponse;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PostControllerTest {

    @Value("${test.authorization}")
    String authorization;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    @DisplayName("Post Upload API Document")
    void signUpTest() throws Exception {
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
                                .header("Authorization", authorization)
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

}