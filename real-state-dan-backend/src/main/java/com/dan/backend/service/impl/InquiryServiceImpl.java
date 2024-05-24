package com.dan.backend.service.impl;

import com.dan.backend.enumSet.PropertyStatus;
import com.dan.backend.repo.InquiryRepo;
import com.dan.backend.repo.PropertyRepo;
import com.dan.backend.repo.UserRepo;
import com.dan.backend.service.InquiryService;
import com.dan.backend.service.UserService;
import com.dan.backend.domain.Inquiry;
import com.dan.backend.domain.Property;
import com.dan.backend.domain.User;
import com.dan.backend.domain.dto.request.InquiryRequestDto;
import com.dan.backend.domain.dto.request.UserRequestDto;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.response.InquiryResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PropertyRepo propertyRepo;
    private final UserRepo userRepo;
    private final InquiryRepo inquiryRepo;


    private final List<PropertyStatus> allowedPropertyStatusForInquiry = Arrays.asList(PropertyStatus.AVAILABLE, PropertyStatus.PENDING);


    @Override
    public HttpResponse save(long propertyId, InquiryRequestDto inquiryRequestDto) {
        if (!userService.isCustomer()) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("Only Customer can send inquiry").build();
        }

        Property property = propertyRepo.findById(propertyId).get();

        if (!allowedPropertyStatusForInquiry.contains(property.getPropertyStatus())) {
            return HttpResponse.builder().status(HttpStatus.FORBIDDEN).message("The property is not available").build();
        }

        Inquiry inquiry = modelMapper.map(inquiryRequestDto, Inquiry.class);
        User user = userRepo.findById(userService.getLoggedInUser().getId()).get();
        inquiry.setUser(user);
        inquiry.setProperty(property);

        inquiryRepo.save(inquiry);
        return HttpResponse.builder().status(HttpStatus.CREATED).message("Success").build();
    }


    @Override
    public List<InquiryResponseDto> findAll() {
        UserRequestDto loggedInUser = userService.getLoggedInUser();
        if (userService.isCustomer()) {
            return inquiryRepo.findAllByUserId(loggedInUser.getId()).stream().map(o -> {
                InquiryResponseDto inquiry = modelMapper.map(o, InquiryResponseDto.class);
                inquiry.setPropertyId(o.getProperty().getId());
                return inquiry;
            }).toList();

        }
        if (userService.isOwner()) {
            return findAllByOwner(loggedInUser.getId());
        }
        return null;
    }

    @Override
    public List<InquiryResponseDto> findAllByOwner(long ownerId) {

        return inquiryRepo.findAllByOwnerId(ownerId).stream().map(i -> {
            InquiryResponseDto inquiry = modelMapper.map(i, InquiryResponseDto.class);
            inquiry.setPropertyId(i.getProperty().getId());
            return inquiry;
        }).toList();
    }
}
