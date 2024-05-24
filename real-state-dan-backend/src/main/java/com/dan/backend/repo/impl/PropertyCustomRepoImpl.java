package com.dan.backend.repo.impl;

import com.dan.backend.repo.PropertyCustomRepo;
import com.dan.backend.domain.Property;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.request.PropertyFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PropertyCustomRepoImpl implements PropertyCustomRepo {

    private final EntityManager entityManager;

    @Override
    public List<Property> findByFilters(PropertyFilterRequest propertyFilterRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> cq = cb.createQuery(Property.class);
        Root<Property> property = cq.from(Property.class);
        Join<Property, User> user = property.join("owner");


        List<Predicate> predicates = new ArrayList<>();

        if (propertyFilterRequest.getPropertyStatus() != null) {
            predicates.add(cb.equal(property.get("propertyStatus"), propertyFilterRequest.getPropertyStatus()));
        }
//        else {
//            predicates.add(cb.notEqual(property.get("propertyStatus"), PropertyStatus.DEAL));
//            predicates.add(cb.notEqual(property.get("propertyStatus"), PropertyStatus.UNAVAILABLE));
//        }

        if (propertyFilterRequest.getCity() != null) {
            predicates.add(cb.like(property.get("city"), "%"+propertyFilterRequest.getCity()+"%"));
        }

        if (propertyFilterRequest.getState() != null) {
            predicates.add(cb.like(property.get("state"), "%"+propertyFilterRequest.getState()+"%"));
        }

        if (propertyFilterRequest.getOwnerId() != null) {
            predicates.add(cb.equal(user.get("id"), propertyFilterRequest.getOwnerId()));
        }


        if (propertyFilterRequest.getPropertyType() != null) {
            predicates.add(cb.equal(property.get("propertyType"), propertyFilterRequest.getPropertyType()));
        }

        if (propertyFilterRequest.getListingType() != null) {
            predicates.add(cb.equal(property.get("listingType"), propertyFilterRequest.getListingType()));
        }

        if (propertyFilterRequest.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("price"), propertyFilterRequest.getMinPrice()));
        }
        if (propertyFilterRequest.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("price"), propertyFilterRequest.getMaxPrice()));
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));


        TypedQuery<Property> query = entityManager.createQuery(cq);
        return query.getResultList();

    }
}
