package com.dan.backend.service;

import com.dan.backend.domain.dto.request.UserRequestDto;
import com.dan.backend.domain.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {

    UserResponseDto findById(long userId);

    List<UserResponseDto> findAll();

    UserRequestDto getLoggedInUser();

    Boolean isAdmin();

    Boolean isOwner();

    Boolean isCustomer();

}
