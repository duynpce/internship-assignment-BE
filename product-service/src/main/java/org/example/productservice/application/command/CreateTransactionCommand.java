package org.example.productservice.application.command;


import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionCommand(

        UUID productId,
        UUID customerId,
        Integer quantity,
        BigDecimal price
) {

}
