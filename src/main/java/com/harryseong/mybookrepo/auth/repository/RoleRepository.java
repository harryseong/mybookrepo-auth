package com.harryseong.mybookrepo.auth.repository;

import com.harryseong.mybookrepo.auth.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
