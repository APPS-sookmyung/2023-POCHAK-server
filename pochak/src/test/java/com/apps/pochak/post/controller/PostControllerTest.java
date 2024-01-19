package com.apps.pochak.post.controller;

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

import static com.apps.pochak.common.ApiDocumentUtils.getDocumentRequest;
import static com.apps.pochak.common.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        String fileName = "APPS_LOGO";
        String fileType = "PNG";

        FileInputStream fileInputStream
                = new FileInputStream("src/test/resources/static/" + fileName + "." + fileType);
        MockMultipartFile testImg = new MockMultipartFile(
                "postImage",
                fileName + "." + fileType,
                "multipart/form-data",
                fileInputStream
        );

        String caption = "안녕하세요. 게시물 업로드를 테스트해보겠습니다.";
        final MockMultipartFile captionPart = new MockMultipartFile(
                "caption",
                "",
                "multipart/form-data",
                caption.getBytes()
        );


        String taggedMemberHandles = "dxxynni, _skf__11";
        final MockMultipartFile taggedMemberHandlesPart = new MockMultipartFile(
                "taggedMemberHandles",
                "",
                "multipart/form-data",
                taggedMemberHandles.getBytes()
        );


        // TODO: caption과 taggedMemberHandles 문서화가 안됨
        this.mockMvc.perform(
                        multipart("/api/v2/posts")
                                .file(testImg)
                                .param("caption", caption)
                                .param("taggedMemberHandles", taggedMemberHandles)
                                .contentType(MULTIPART_FORM_DATA_VALUE)
                                .header("Authorization", authorization)
                                .characterEncoding("UTF-8")
                ).andExpect(status().isOk())
                .andDo(
                        document("upload-post",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic auth credentials")
                                ),
                                requestParts(
                                        partWithName("postImage").description("업로드 할 게시물 사진 파일 : 빈 파일 전달 시 에러 발생")
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