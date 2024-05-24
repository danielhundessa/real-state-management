package com.dan.backend.domain;

import com.dan.backend.enumSet.OfferStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    private Double price;

    @OneToOne
    private User user;

    @ManyToOne
    private Property property;


}
