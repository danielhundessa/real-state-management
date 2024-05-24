package com.dan.backend.repo;

import com.dan.backend.domain.FavouriteProperty;
import com.dan.backend.domain.Property;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface FavouriteRepo extends CrudRepository<FavouriteProperty, Long> {
    @Query("select f.property from FavouriteProperty f join f.customer u where u.id=:userId and f.property.propertyStatus!='UNAVAILABLE'")
    List<Property> findFavouritesByCustomer(@Param("userId") long userId);

    @Modifying
    @Query("delete from FavouriteProperty f where f.propertyId=:propertyId and f.customerId=:customerId")
    void deleteFavouritePropertyByPropertyIdAndAndCustomerId(long propertyId, long customerId);

}
