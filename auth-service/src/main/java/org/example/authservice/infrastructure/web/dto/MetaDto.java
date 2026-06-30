package org.example.authservice.infrastructure.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaDto {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public static class MetaDtoBuilder{
        public MetaDtoBuilder paginationDto(PaginationDto paginationDto) {
            if(paginationDto == null) return this;

            this.currentPage = paginationDto.getPage();
            this.pageSize = paginationDto.getLimit();
            return this;
        }
    }

}
