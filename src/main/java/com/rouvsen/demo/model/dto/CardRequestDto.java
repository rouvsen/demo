package com.rouvsen.demo.model.dto;

public record CardRequestDto(
        String pan,
        Integer customerId
) {
}
