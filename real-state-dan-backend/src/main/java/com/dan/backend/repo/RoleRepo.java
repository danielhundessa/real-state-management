package com.dan.backend.repo;

import com.dan.backend.enumSet.RoleType;
import com.dan.backend.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role,Long> {

//    Role findByRole(String role);

    Role findByRole(RoleType role);
}
