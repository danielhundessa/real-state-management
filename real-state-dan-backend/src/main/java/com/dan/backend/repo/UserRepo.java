package com.dan.backend.repo;

import com.dan.backend.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u join u.roles r where  r.role != 'ADMIN'")
    List<User> findAllExceptAdmin();


}
