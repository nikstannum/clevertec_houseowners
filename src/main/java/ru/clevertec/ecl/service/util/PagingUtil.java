package ru.clevertec.ecl.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagingUtil {

    @Value("${spring.data.web.pageable.max-page-size}")
    private Integer maxPageSize;

    public Paging getPaging(Integer page, Integer size) {
        int limit;
        if (size == null || size > maxPageSize) {
            limit = maxPageSize;
        } else {
            limit = size;
        }
        if (page == null || page < 1) {
            page = 1;
        }
        int offset = (page - 1) * limit;
        return new Paging(limit, offset);
    }

    public record Paging(int limit, int offset) {
    }
}
