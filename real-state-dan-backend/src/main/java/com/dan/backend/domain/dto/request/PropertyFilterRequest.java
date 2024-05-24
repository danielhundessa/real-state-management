package com.dan.backend.domain.dto.request;

import com.dan.backend.enumSet.ListingType;
import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.enumSet.PropertyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyFilterRequest {
    private Double minPrice;
    private Double maxPrice;
    private ListingType listingType;
    private PropertyType propertyType;
    private Long ownerId;
    private PropertyStatus propertyStatus;
    private String state;
    private String city;
}
