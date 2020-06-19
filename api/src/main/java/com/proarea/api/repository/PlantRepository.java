package com.proarea.api.repository;

import com.proarea.api.model.entity.PlantEntity;
import com.proarea.api.model.entity.PlantRequestEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlantRepository extends CrudRepository<PlantEntity, Long> {

    PlantEntity findFirstByName(String name);

}
