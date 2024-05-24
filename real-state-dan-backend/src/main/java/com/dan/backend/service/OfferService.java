package com.dan.backend.service;

import com.dan.backend.enumSet.OfferStatus;
import com.dan.backend.domain.dto.request.OfferRequestDto;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.response.OfferResponseDto;

import java.util.List;

public interface OfferService {
    HttpResponse save(long propertyId, OfferRequestDto offerDto);

    List<OfferResponseDto> findAll();
    List<OfferResponseDto> findAllByOwner(long ownerId);

    HttpResponse updateStatus(long offerId, OfferStatus status);
}
