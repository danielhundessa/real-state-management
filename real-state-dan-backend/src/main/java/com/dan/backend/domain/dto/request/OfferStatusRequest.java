package com.dan.backend.domain.dto.request;

import com.dan.backend.enumSet.OfferStatus;
import lombok.Data;

@Data
public class OfferStatusRequest {
    OfferStatus status;
}
