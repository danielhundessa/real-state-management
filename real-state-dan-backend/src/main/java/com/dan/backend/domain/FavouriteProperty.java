package com.dan.backend.domain;

import com.dan.backend.domain.compositeKey.FaviouritePK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="favourite_properties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FaviouritePK.class)

public class FavouriteProperty {
        @Id
        @Column(name = "customer_id")
        private long customerId;

        @Id
        @Column(name = "property_id")
        private long propertyId;

        @ManyToOne
        @JoinColumn(name = "customer_id", insertable = false, updatable = false)
        private User customer;

        @ManyToOne
        @JoinColumn(name = "property_id", insertable = false, updatable = false)
        private Property property;
}
