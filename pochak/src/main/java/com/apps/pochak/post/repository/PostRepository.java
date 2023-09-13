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
        /*
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(postPK));
        eav.put(":val2", new AttributeValue().withS("POST#"));

        DynamoDBQueryExpression<Post> query = new DynamoDBQueryExpression<Post>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<Post> posts = mapper.query(Post.class, query); // 하나만 반환할것이므로
        if (posts.isEmpty()) {
            throw new BaseException(INVALID_POST_ID);
        }
        return posts.get(0);
        */

        return postCrudRepository.findPostByPostPKAndAllowedDateStartingWith(postPK, "POST#")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST_ID));
    }

}



