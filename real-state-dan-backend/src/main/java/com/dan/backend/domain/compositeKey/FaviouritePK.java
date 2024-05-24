package com.dan.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FaviouritePK implements Serializable {
    @Id
    @Column(name = "customer_id")
    private long customerId;

    @Id
    @Column(name = "property_id")
    private long propertyId;

}
