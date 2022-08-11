package com.magadiflo.app.repository;

import com.magadiflo.app.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface IRoleRepository extends CrudRepository<Role, Long> {
}
