package com.apps.pochak.global.converter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableToPageRequestConverter {
    public static PageRequest toPageRequest(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Sort sort = pageable.getSort();

        return PageRequest.of(page, size, sort);
    }
}
