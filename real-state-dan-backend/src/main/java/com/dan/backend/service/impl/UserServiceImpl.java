package com.dan.backend.service.impl;

import com.dan.backend.enumSet.RoleType;
import com.dan.backend.repo.UserRepo;
import com.dan.backend.service.UserService;
import com.dan.backend.domain.Property;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.PropertyDto;
import com.dan.backend.domain.dto.request.UserRequestDto;
import com.dan.backend.domain.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username - %s not found", username)));

    }

    @Override
    public UserResponseDto findById(long userId) {
        User user = userRepo.findById(userId).get();
        UserResponseDto results = UserResponseDto.builder().id(user.getId()).email(user.getEmail()).name(user.getName()).build();

        if (isOwner()) {

            List<PropertyDto> properties = new ArrayList<>();
            user.getProperties().forEach(p -> {
                properties.add(PropertyDto.builder().price(p.getPrice()).state(p.getState()).address(p.getAddress()).id(p.getId())
                        .city(p.getCity()).listingType(p.getListingType()).propertyType(p.getPropertyType()).image(p.getImage())
                        .ownerId(p.getOwner().getId()).title(p.getTitle()).zipCode(p.getZipCode())

                        .build());
            });
            results.setProperties(properties);
        }

        if (isCustomer()) {

            List<PropertyDto> favourites = new ArrayList<>();
            user.getFavourites().forEach(f -> {
                Property p = f.getProperty();
                favourites.add(PropertyDto.builder().price(p.getPrice()).state(p.getState()).address(p.getAddress()).id(p.getId())
                        .city(p.getCity()).listingType(p.getListingType()).propertyType(p.getPropertyType()).image(p.getImage())
                        .ownerId(p.getOwner().getId()).title(p.getTitle()).zipCode(p.getZipCode())
                        .build());
            });
            results.setProperties(favourites);
        }


        return results;
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<UserResponseDto> users = new ArrayList<>();
        userRepo.findAllExceptAdmin().forEach(u -> {
            UserResponseDto user = modelMapper.map(u, UserResponseDto.class);
            user.setRole(u.getRoles().get(0).getRole());
            users.add(user);
        });
        return users;
    }

    @Override
    public UserRequestDto getLoggedInUser() {
        try {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return modelMapper.map(loggedInUser, UserRequestDto.class);

        } catch (Exception e) {
            System.out.println("No Logged in User");
            return null;
        }

    }

    @Override
    public Boolean isAdmin() {
        return userRepo.findById(getLoggedInUser().getId()).get().getRoles().stream().anyMatch(u -> u.getRole() == RoleType.ADMIN);
    }

    @Override
    public Boolean isOwner() {
        return userRepo.findById(getLoggedInUser().getId()).get().getRoles().stream().anyMatch(u -> u.getRole() == RoleType.OWNER);
    }

    @Override
    public Boolean isCustomer() {
        return userRepo.findById(getLoggedInUser().getId()).get().getRoles().stream().anyMatch(u -> u.getRole() == RoleType.CUSTOMER);
    }
}
