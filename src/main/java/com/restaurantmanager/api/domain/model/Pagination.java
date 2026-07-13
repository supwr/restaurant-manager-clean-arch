package com.restaurantmanager.api.domain.model;

import java.util.Objects;

public class Pagination {

    private final Integer page;
    private final Integer size;
    private final String sortBy;
    private final SortDirection sortDirection;

    public enum SortDirection {
        ASC, DESC
    }

    public Pagination(final Integer page, final Integer size, final String sortBy, final SortDirection sortDirection) {
        this.page = Objects.requireNonNull(page, "page must not be null");
        this.size = Objects.requireNonNull(size, "size must not be null");
        this.sortBy = Objects.requireNonNull(sortBy, "sortBy must not be null");
        this.sortDirection = Objects.requireNonNull(sortDirection, "sortDirection must not be null");
    }

    public Pagination(final Integer page, final Integer size, final String sortBy) {
        this(page, size, sortBy, SortDirection.ASC);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Pagination that = (Pagination) o;
        return page.equals(that.page)
                && size.equals(that.size)
                && sortBy.equals(that.sortBy)
                && sortDirection == that.sortDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size, sortBy, sortDirection);
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection=" + sortDirection +
                '}';
    }
}

