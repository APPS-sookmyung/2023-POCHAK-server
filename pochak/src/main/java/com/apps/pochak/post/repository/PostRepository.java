package com.apps.pochak.post.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final PostCrudRepository postCrudRepository;
    private final DynamoDBMapper mapper;

    public void deletePost(Post post){
        postCrudRepository.delete(post);
    }

    public Post savePost(Post post) {
        return postCrudRepository.save(post);
    }

    // PK,SK에 둘다 POST PK를 넣어서 Post 객체 전달하면 완료
    public Post findPostByPostPK(String postPK) throws BaseException {
        return postCrudRepository.findPostByPostPKAndAllowedDateStartingWith(postPK, "POST#")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST_ID));
    }

}



