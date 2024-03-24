package com.example.lab.dto;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class PageDto<T> {
    private List<T> content;

    private int size;
    private long number;
    private long totalPages;

    public PageDto(List<T> content, Pageable pageable, long totalPages) {
        this.content = content;
        this.size = pageable.getPageSize();
        this.number = pageable.getPageNumber();
        this.totalPages = totalPages;
    }
}
