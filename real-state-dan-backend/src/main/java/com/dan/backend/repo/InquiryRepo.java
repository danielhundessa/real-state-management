package com.dan.backend.repo;

import com.dan.backend.domain.Inquiry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepo extends CrudRepository<Inquiry, Long> {
    List<Inquiry> findAllByUserId(long userId);

    @Query("select i from Inquiry i join i.property p join p.owner u where u.id=:ownerId")
    List<Inquiry> findAllByOwnerId(@Param("ownerId") long ownerId);

}
