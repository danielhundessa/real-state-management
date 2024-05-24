package com.dan.backend.service;

import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.domain.dto.request.PropertyFilterRequest;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.PropertyDto;

import java.util.List;

public interface PropertyService {


    void save(PropertyDto propertyDto);

    void favourite(long propertyId);

    void removeFavourite(long propertyId);

    List<PropertyDto> findFavouritesByCustomer();


    List<PropertyDto> findAll(PropertyFilterRequest propertyFilterRequest);

    PropertyDto findById(long id);


    HttpResponse update(long id, PropertyDto propertyDto);

    HttpResponse delete(long id);

    HttpResponse publish(long id);

    HttpResponse cancelContigency(long id);

    HttpResponse upgradeToContigency(long id);

    void updateStatus(long propertyId, PropertyStatus status);

}
