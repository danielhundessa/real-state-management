package com.dan.backend.service.impl;

import com.dan.backend.enumSet.OfferStatus;
import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.repo.OfferRepo;
import com.dan.backend.repo.PropertyRepo;
import com.dan.backend.repo.UserRepo;
import com.dan.backend.service.EmailService;
import com.dan.backend.service.OfferService;
import com.dan.backend.service.PropertyService;
import com.dan.backend.service.UserService;
import com.dan.backend.domain.Offer;
import com.dan.backend.domain.Property;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.request.OfferRequestDto;
import com.dan.backend.domain.dto.request.UserRequestDto;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.response.OfferResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PropertyService propertyService;
    private final UserRepo userRepo;
    private final OfferRepo offerRepo;
    private final PropertyRepo propertyRepo;
    private final EmailService emailService;


    private final List<PropertyStatus> allowedPropertyStatusForOffer = Arrays.asList(PropertyStatus.AVAILABLE, PropertyStatus.PENDING);
    private final List<OfferStatus> allowedOfferStatusForCustomer = Arrays.asList(OfferStatus.CANCELLED);
    private final List<OfferStatus> allowedOfferStatusForOwner = Arrays.asList(OfferStatus.APPROVED, OfferStatus.CANCELLED);

    @Override
    public HttpResponse save(long propertyId, OfferRequestDto offerDto) {

        if (!userService.isCustomer()) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("Only Customer can send offer").build();

        }
        Property property = propertyRepo.findById(propertyId).get();
        if (!allowedPropertyStatusForOffer.contains(property.getPropertyStatus())) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("The property is not available").build();
        }

        Offer offer = modelMapper.map(offerDto, Offer.class);
        User user = userRepo.findById(userService.getLoggedInUser().getId()).get();
        offer.setUser(user);
        offer.setProperty(property);
        offer.setOfferStatus(OfferStatus.PENDING);
        offerRepo.save(offer);
        emailService.send(property.getOwner().getEmail(), "Your Property - " + property.getTitle() + " has an offer", offer.getUser().getName() + " has offered you " + offer.getPrice());
        return HttpResponse.builder().status(HttpStatus.CREATED).message("Success").build();
    }

    @Override
    public List<OfferResponseDto> findAll() {
        UserRequestDto loggedInUser = userService.getLoggedInUser();
        if (userService.isCustomer()) {
            return offerRepo.findAllByUserId(loggedInUser.getId()).stream().map(o -> {
                OfferResponseDto offer = modelMapper.map(o, OfferResponseDto.class);
                offer.setPropertyId(o.getProperty().getId());
                offer.setPropertyStatus(o.getProperty().getPropertyStatus());
                return offer;
            }).toList();
        }
        if (userService.isOwner()) {
            return findAllByOwner(loggedInUser.getId());
        }
        return null;
    }

    @Override
    public List<OfferResponseDto> findAllByOwner(long ownerId) {

        return offerRepo.findAllByOwnerId(ownerId).stream().map(o -> {
            OfferResponseDto offer = modelMapper.map(o, OfferResponseDto.class);
            offer.setPropertyId(o.getProperty().getId());
            offer.setPropertyStatus(o.getProperty().getPropertyStatus());
            return offer;
        }).toList();
    }

    @Override
    public HttpResponse updateStatus(long offerId, OfferStatus status) {
        Offer offer = offerRepo.findById(offerId).get();

        if (userService.isOwner() && !allowedOfferStatusForOwner.contains(status)) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action is not permitted").build();
        }

        if (userService.isCustomer() && !allowedOfferStatusForCustomer.contains(status) && offer.getProperty().getPropertyStatus() != PropertyStatus.CONTINGENT) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("This action is not permitted").build();
        }

        offer.setOfferStatus(status);

        //sync with product status
        if (userService.isOwner()) {
            if (status.equals(OfferStatus.APPROVED)) {
                emailService.send(offer.getUser().getEmail(), "Your Offer for - " + offer.getProperty().getTitle() + " has been approved", offer.getProperty().getOwner().getName() + " has approved your offer");

                propertyService.updateStatus(offer.getProperty().getId(), PropertyStatus.PENDING);
            }
            if (status.equals(OfferStatus.REJECTED)) {
                emailService.send(offer.getUser().getEmail(), "Your Offer for - " + offer.getProperty().getTitle() + " has been rejected", offer.getProperty().getOwner().getName() + " has rejected your offer");

                propertyService.updateStatus(offer.getProperty().getId(), PropertyStatus.AVAILABLE);
            }
        }

        //save offer
        offerRepo.save(offer);
        return HttpResponse.builder().status(HttpStatus.OK).message("Success").build();

    }
}
