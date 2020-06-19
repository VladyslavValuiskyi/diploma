package com.proarea.api.repository;

import com.proarea.api.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findFirstByUsername(String username);

    UserEntity findFirstById(Long id);

    List<UserEntity> findAllByEnabled(Boolean enabled);

}
