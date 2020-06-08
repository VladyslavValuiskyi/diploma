package com.proarea.api.repository;

import com.proarea.api.model.entity.AuthoritiesEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<AuthoritiesEntity, Long> {
}
