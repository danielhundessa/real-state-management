package com.dan.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "inquiries")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @OneToOne
    private User user;

    @ManyToOne
    private Property property;
}
