package com.rouvsen.demo.model.dto;

public record CardResponseDto(
        Integer id,
        String pan,
        Integer customerId,
        Double balance
) {
}
