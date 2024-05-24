package com.dan.backend.domain.dto.response;

import com.dan.backend.enumSet.OfferStatus;
import com.dan.backend.enumSet.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponseDto {
    private long id;
    private String message;
    private OfferStatus offerStatus;
    private PropertyStatus propertyStatus;
    private Double price;
    private long propertyId;

}
