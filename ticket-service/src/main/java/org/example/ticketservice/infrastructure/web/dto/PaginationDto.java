package org.example.ticketservice.infrastructure.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {

    @NotNull(message = "page is required")
    @Min(value = 0, message = "page cannot be negative")
    private Integer page;

    @NotNull(message = "limit is required")
    @Min(value = 1, message = "limit must be greater than 0")
    @Max(value = 1000, message = "limit cannot be greater than 5000")
    private Integer limit;
}
