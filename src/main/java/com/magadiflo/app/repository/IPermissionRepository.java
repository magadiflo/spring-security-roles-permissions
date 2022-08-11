package com.magadiflo.app.repository;

import com.magadiflo.app.domain.Permission;
import org.springframework.data.repository.CrudRepository;

public interface IPermissionRepository extends CrudRepository<Permission, Long> {

    Permission findByName(String name);

}
