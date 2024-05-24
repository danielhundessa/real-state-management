package com.dan.backend.controller;

import com.dan.backend.domain.dto.request.OfferStatusRequest;
import com.dan.backend.domain.dto.response.HttpResponse;
import com.dan.backend.domain.dto.response.OfferResponseDto;
import com.dan.backend.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OfferResponseDto> findAll() {
        return offerService.findAll();
    }

    @GetMapping("/owner/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OfferResponseDto> findAllByOwner(@PathVariable long id) {
        return offerService.findAllByOwner(id);
    }

    @PutMapping("/{id}")
    public HttpResponse update(@PathVariable long id, @RequestBody OfferStatusRequest request) {
        return offerService.updateStatus(id, request.getStatus());
    }
}
