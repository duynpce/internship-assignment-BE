package org.example.userservice.application.command;

import java.util.Collections;
import java.util.List;

public final class PageCommand<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int number;   // zero-based page index, mirrors Spring's Page#getNumber()
    private final int size;     // page size, mirrors Spring's Page#getSize()

    private PageCommand(List<T> content, long totalElements, int totalPages, int number, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
    }

    public static <T> PageCommand<T> of(List<T> content, long totalElements, int number, int size) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return new PageCommand<>(List.copyOf(content), totalElements, totalPages, number, size);
    }

    public static <T> PageCommand<T> empty(int number, int size) {
        return new PageCommand<>(Collections.emptyList(), 0L, 0, number, size);
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean isFirst() {
        return number == 0;
    }

    public boolean isLast() {
        return totalPages == 0 || number >= totalPages - 1;
    }

    public boolean hasNext() {
        return number + 1 < totalPages;
    }

    public boolean hasPrevious() {
        return number > 0;
    }
}
