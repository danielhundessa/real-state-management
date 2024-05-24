package com.dan.backend.repo;

import com.dan.backend.domain.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepo extends CrudRepository<Offer, Long> {

    List<Offer> findAllByUserId(long userId);

    @Query("select o from Offer o join o.property p join p.owner u where u.id=:ownerId")
    List<Offer> findAllByOwnerId(@Param("ownerId") long ownerId);

}
