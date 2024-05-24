package com.dan.backend.controller;

import com.dan.backend.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favourites")
public class FavouriteController {

    private final PropertyService propertyService;

    @GetMapping

    @ResponseStatus(HttpStatus.OK)
    public void findFavouritesByCustomer() {
        propertyService.findFavouritesByCustomer();
    }

}
