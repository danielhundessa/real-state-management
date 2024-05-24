package com.dan.backend.service.impl;

import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.repo.FavouriteRepo;
import com.dan.backend.repo.PropertyRepo;
import com.dan.backend.repo.UserRepo;
import com.dan.backend.service.PropertyService;
import com.dan.backend.service.UserService;
import com.dan.backend.domain.FavouriteProperty;
import com.dan.backend.domain.Property;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.PropertyDto;
import com.dan.backend.domain.dto.request.PropertyFilterRequest;
import com.dan.backend.domain.dto.request.UserRequestDto;
import com.dan.backend.domain.dto.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepo propertyRepo;
    private final UserRepo userRepo;
    private final FavouriteRepo favouriteRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public List<PropertyDto> findAll(PropertyFilterRequest propertyFilterRequest) {
        List<PropertyDto> results = new ArrayList<>();
        propertyRepo.findByFilters(propertyFilterRequest).forEach(p -> results.add(modelMapper.map(p, PropertyDto.class)));
        return results;
    }

    @Override
    public PropertyDto findById(long id) {
        return modelMapper.map(propertyRepo.findById(id), PropertyDto.class);
    }

    @Override
    public void save(PropertyDto propertyDto) {
        Property property = modelMapper.map(propertyDto, Property.class);
        User owner = userRepo.findById(userService.getLoggedInUser().getId()).get();
        property.setOwner(owner);
        property.setPropertyStatus(PropertyStatus.UNAVAILABLE);
        propertyRepo.save(property);
    }

    @Override

    public void favourite(long propertyId) {
        long userid = userService.getLoggedInUser().getId();
        favouriteRepo.save(FavouriteProperty.builder().propertyId(propertyId).customerId(userid).build());
    }

    @Override
    public List<PropertyDto> findFavouritesByCustomer() {
        long userid = userService.getLoggedInUser().getId();
        return favouriteRepo.findFavouritesByCustomer(userid).stream().map(p -> modelMapper.map(p, PropertyDto.class)).toList();
    }

    @Override
    public void removeFavourite(long propertyId) {
        long customerId = userService.getLoggedInUser().getId();
        favouriteRepo.deleteFavouritePropertyByPropertyIdAndAndCustomerId(propertyId, customerId);
    }


    public HttpResponse update(long id, PropertyDto propertyDto) {
        Property propertyDB = propertyRepo.findById(id).get();
        var criticalStatus = Arrays.asList(PropertyStatus.PENDING, PropertyStatus.CONTINGENT);
        if (criticalStatus.contains(propertyDB.getPropertyStatus())) {

        }
        Property property = modelMapper.map(propertyDto, Property.class);
        property.setId(id);
        property.setPropertyStatus(propertyDB.getPropertyStatus());
        property.setOwner(propertyDB.getOwner());
        propertyRepo.save(property);
        return HttpResponse.builder().status(HttpStatus.OK).message("The property is deleted").build();

    }

    @Override
    public HttpResponse delete(long id) {
        Property property = propertyRepo.findById(id).get();
        var criticalStatus = Arrays.asList(PropertyStatus.PENDING, PropertyStatus.CONTINGENT);
        if (criticalStatus.contains(property.getPropertyStatus())) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action is not permitted").build();

        }
        propertyRepo.delete(property);
        return HttpResponse.builder().status(HttpStatus.OK).message("The property is deleted").build();
    }

    public boolean isOwner(long propertyOwnerId) {
        UserRequestDto loggedIn = userService.getLoggedInUser();
        return propertyOwnerId == loggedIn.getId();
    }


    @Override
    public HttpResponse publish(long id) {
        try {
            if (!userService.isAdmin()) {
                return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action is not permitted").build();

            }
            Property property = propertyRepo.findById(id).get();
            property.setPropertyStatus(PropertyStatus.AVAILABLE);
            propertyRepo.save(property);
            return HttpResponse.builder().status(HttpStatus.OK).message("Property Published").build();
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpResponse upgradeToContigency(long id) {
        try {
            if (!userService.isOwner()) {
                return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action can only be performed by owner").build();

            }
            Property property = propertyRepo.findById(id).get();
            if (property.getPropertyStatus() != PropertyStatus.PENDING) {
                return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action can only be done for pending status").build();

            }
            property.setPropertyStatus(PropertyStatus.CONTINGENT);
            propertyRepo.save(property);
            return HttpResponse.builder().status(HttpStatus.OK).message("Property Upgraded to contingent").build();
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpResponse cancelContigency(long id) {
        try {
            if (!userService.isOwner()) {
                return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action can only be performed by owner").build();

            }
            Property property = propertyRepo.findById(id).get();
            if (property.getPropertyStatus() != PropertyStatus.CONTINGENT) {
                return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action can only be done for contingent status").build();

            }
            property.setPropertyStatus(PropertyStatus.AVAILABLE);
            //set any offers for this property to
            propertyRepo.save(property);
            return HttpResponse.builder().status(HttpStatus.OK).message("Property is available to customers").build();
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(long propertyId, PropertyStatus status) {
        propertyRepo.updatePropertyStatus(propertyId, status);
    }

}
