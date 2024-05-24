package com.dan.backend.domain.dto.response;

import com.dan.backend.enumSet.ListingType;
import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.enumSet.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyUserResponseDto {
    private long id;
    private String title;
    private String description;
    private Double price;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String image;
    private ListingType listingType;
    private PropertyType propertyType;
    private PropertyStatus propertyStatus;
    private Boolean isFavourite;
    private Long ownerId;
}
