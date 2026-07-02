package org.example.userservice.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.infrastructure.web.dto.PaginationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
