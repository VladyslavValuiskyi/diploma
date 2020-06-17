package com.proarea.api.repository;

import com.proarea.api.model.entity.AuthoritiesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthoritiesRepository extends CrudRepository<AuthoritiesEntity, Long> {

    @Query(value = "select authority from authorities where user_id = ?1", nativeQuery = true)
    List<String> findAllByUserId(Long id);

}
