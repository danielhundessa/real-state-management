package com.dan.backend.domain.dto.response;

import lombok.Data;

@Data
public class InquiryResponseDto {
    private long id;
    private String message;

    private long propertyId;
}
