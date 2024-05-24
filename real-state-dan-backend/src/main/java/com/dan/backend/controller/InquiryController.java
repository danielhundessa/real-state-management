package com.dan.backend.controller;

import com.dan.backend.domain.dto.response.InquiryResponseDto;
import com.dan.backend.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InquiryResponseDto> findAll() {
        return inquiryService.findAll();
    }


}
