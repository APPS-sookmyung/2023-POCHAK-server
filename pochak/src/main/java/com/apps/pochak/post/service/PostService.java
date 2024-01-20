package com.apps.pochak.post.service;

import com.apps.pochak.alarm.domain.TagApprovalAlarm;
import com.apps.pochak.alarm.domain.repository.AlarmRepository;
import com.apps.pochak.global.s3.S3Service;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.repository.MemberRepository;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import com.apps.pochak.post.dto.request.PostUploadRequest;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.global.s3.DirName.POST;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;
    private final TagRepository tagRepository;
    private final JwtService jwtService;
    private final S3Service s3Service;

    @Transactional
    public void savePost(
            final MultipartFile postImage,
            final PostUploadRequest request
    ) {
        final Member loginMember = jwtService.getLoginMember();
        final String image = s3Service.upload(postImage, POST);
        final Post post = request.toEntity(image, loginMember);
        postRepository.save(post);
        final List<String> taggedMemberHandles = request.getTaggedMemberHandleList();

        // TODO: N+1 고치기
        final List<Member> taggedMemberList = taggedMemberHandles.stream().map(
                memberRepository::findByHandle
        ).collect(Collectors.toList());

        final List<Tag> tagList = taggedMemberList.stream().map(
                member -> Tag.builder()
                        .member(member)
                        .post(post)
                        .build()
        ).collect(Collectors.toList());
        tagRepository.saveAll(tagList);

        final List<TagApprovalAlarm> tagApprovalAlarmList = tagList.stream().map(
                tag -> TagApprovalAlarm.builder()
                        .tag(tag)
                        .receiver(tag.getMember())
                        .build()
        ).collect(Collectors.toList());
        alarmRepository.saveAll(tagApprovalAlarmList);
    }
}
