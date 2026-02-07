package com.mom.storefront_panoply.tools;

import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;


    public static <T> PagedResponse<T> from(Page<T> page) {

        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .totalElements(page.getTotalElements()).build();
    }

    public static <E, T> PagedResponse<T> from(Page<E> page, Function<? super E, ? extends T> mapper) {
        Page<T> mapped = page.map(mapper);
        return from(mapped);
    }

}
