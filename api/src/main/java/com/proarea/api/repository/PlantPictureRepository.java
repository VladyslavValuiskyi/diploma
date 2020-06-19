package com.proarea.api.repository;

import com.proarea.api.model.entity.PlantEntity;
import com.proarea.api.model.entity.PlantPictureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantPictureRepository extends CrudRepository<PlantPictureEntity, Long> {

    List<PlantPictureEntity> findAllByPlantId(Long id);

}
