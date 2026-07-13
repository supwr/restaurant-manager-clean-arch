package com.restaurantmanager.api.domain.model;

import java.util.List;
import java.util.Objects;

public class PageResult<T> {

    private final List<T> content;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final Long totalElements;
    private final Integer totalPages;
    private final Boolean hasNextPage;
    private final Boolean hasPreviewPage;

    public PageResult(
            final List<T> content,
            final Integer pageNumber,
            final Integer pageSize,
            final Long totalElements,
            final Integer totalPages
    ) {
        this.content = Objects.requireNonNull(content, "content must not be null");
        this.pageNumber = Objects.requireNonNull(pageNumber, "pageNumber must not be null");
        this.pageSize = Objects.requireNonNull(pageSize, "pageSize must not be null");
        this.totalElements = Objects.requireNonNull(totalElements, "totalElements must not be null");
        this.totalPages = Objects.requireNonNull(totalPages, "totalPages must not be null");
        this.hasNextPage = pageNumber < (totalPages - 1);
        this.hasPreviewPage = pageNumber > 0;
    }

    public List<T> getContent() {
        return content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public Boolean getHasPreviewPage() {
        return hasPreviewPage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PageResult<?> that = (PageResult<?>) o;
        return content.equals(that.content)
                && pageNumber.equals(that.pageNumber)
                && pageSize.equals(that.pageSize)
                && totalElements.equals(that.totalElements)
                && totalPages.equals(that.totalPages)
                && hasNextPage.equals(that.hasNextPage)
                && hasPreviewPage.equals(that.hasPreviewPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, pageNumber, pageSize, totalElements, totalPages, hasNextPage, hasPreviewPage);
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "content=" + content +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", hasNextPage=" + hasNextPage +
                ", hasPreviewPage=" + hasPreviewPage +
                '}';
    }
}

