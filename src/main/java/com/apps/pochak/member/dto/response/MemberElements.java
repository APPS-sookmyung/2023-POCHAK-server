package com.apps.pochak.member.dto.response;

import com.apps.pochak.global.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberElements {
    private PageInfo pageInfo;
    private List<MemberElement> memberList;

    @Builder(builderMethodName = "from")
    public MemberElements(final Page<MemberElement> memberElementPage) {
        this.pageInfo = new PageInfo(memberElementPage);
        this.memberList = memberElementPage.getContent();
    }
}
