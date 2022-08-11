package com.magadiflo.app.repository;

import com.magadiflo.app.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<User, Long> {
}
