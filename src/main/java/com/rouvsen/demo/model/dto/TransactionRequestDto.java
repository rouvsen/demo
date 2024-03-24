package com.rouvsen.demo.model.dto;

public record TransactionRequestDto(
        Double amount,
        String type,
        boolean hasCashback
) {
}
