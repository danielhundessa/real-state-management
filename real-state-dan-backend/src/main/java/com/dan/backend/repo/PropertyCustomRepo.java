package com.dan.backend.repo;

import com.dan.backend.domain.Property;
import com.dan.backend.domain.dto.request.PropertyFilterRequest;

import java.util.List;

public interface PropertyCustomRepo {
    List<Property> findByFilters(PropertyFilterRequest propertyFilterRequest);
}
