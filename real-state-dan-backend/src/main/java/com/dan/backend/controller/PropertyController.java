package com.dan.backend.controller;

import com.dan.backend.domain.dto.PropertyDto;
import com.dan.backend.domain.dto.request.InquiryRequestDto;
import com.dan.backend.domain.dto.request.OfferRequestDto;
import com.dan.backend.domain.dto.request.PropertyFilterRequest;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.enumSet.ListingType;
import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.enumSet.PropertyType;
import com.dan.backend.service.InquiryService;
import com.dan.backend.service.OfferService;
import com.dan.backend.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final OfferService offerService;
    private final InquiryService inquiryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public List<PropertyDto> findAll(@RequestParam(name = "min_price", required = false) Double minPrice, @RequestParam(name = "max_price", required = false) Double maxPrice, @RequestParam(name = "listing_type", required = false) ListingType listingType, @RequestParam(name = "property_type", required = false) PropertyType propertyType, @RequestParam(name = "owner_id", required = false) Long ownerId, @RequestParam(name = "property_status", required = false) PropertyStatus propertyStatus, @RequestParam(name = "city", required = false) String city, @RequestParam(name = "state", required = false) String state

    ) {
        PropertyFilterRequest filters = PropertyFilterRequest.builder().minPrice(minPrice).maxPrice(maxPrice).listingType(listingType).propertyType(propertyType).city(city).state(state).propertyStatus(propertyStatus).ownerId(ownerId).build();

        return propertyService.findAll(filters);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PropertyDto findById(@PathVariable long id) {
        return propertyService.findById(id);
    }

    @PostMapping("/{id}/favourite")
    public void favourite(@PathVariable long id) {
        propertyService.favourite(id);
    }

    @DeleteMapping("/{id}/favourite")
    public void removeFavourite(@PathVariable long id) {
        propertyService.removeFavourite(id);
    }

    @PostMapping("/{id}/offer")
    public HttpResponse offer(@PathVariable long id, @RequestBody OfferRequestDto offerDto) {
        return offerService.save(id, offerDto);
    }

    @PostMapping("/{id}/inquiry")
    public HttpResponse inquiry(@PathVariable long id, @RequestBody InquiryRequestDto inquiryRequestDto) {
        return inquiryService.save(id, inquiryRequestDto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody PropertyDto propertyDto) {
        propertyService.save(propertyDto);
    }


    @PutMapping("/{id}/publish")
    public HttpResponse publish(@PathVariable long id) {
        return propertyService.publish(id);
    }

    @PutMapping("/{id}/cancel-contingent")
    public HttpResponse cancelContingent(@PathVariable long id) {
        return propertyService.cancelContigency(id);
    }

    @PutMapping("/{id}/contingent")
    public HttpResponse upgradeToContingent(@PathVariable long id) {
        return propertyService.upgradeToContigency(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpResponse update(@PathVariable long id, @RequestBody PropertyDto propertyDto) {
        return propertyService.update(id, propertyDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpResponse delete(@PathVariable long id) {
        return propertyService.delete(id);
    }

}
