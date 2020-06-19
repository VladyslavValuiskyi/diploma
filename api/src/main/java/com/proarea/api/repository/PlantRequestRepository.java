package com.proarea.api.repository;

import com.proarea.api.model.entity.AuthoritiesEntity;
import com.proarea.api.model.entity.PlantRequestEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantRequestRepository extends CrudRepository<PlantRequestEntity, Long> {

    PlantRequestEntity findFirstById(Long id);

    List<PlantRequestEntity> findAllByStatus(String status);

}
