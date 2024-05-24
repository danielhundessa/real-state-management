package com.dan.backend.service;

import com.dan.backend.domain.dto.request.InquiryRequestDto;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.response.InquiryResponseDto;

import java.util.List;

public interface InquiryService {
    HttpResponse save(long propertyId, InquiryRequestDto inquiryRequestDto);

    List<InquiryResponseDto> findAllByOwner(long ownerId);

    List<InquiryResponseDto> findAll();

}
