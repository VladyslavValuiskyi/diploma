package com.proarea.api.repository;

import com.proarea.api.model.entity.PictureEntity;
import com.proarea.api.model.entity.PlantRequestPictureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantRequestPictureRepository extends CrudRepository<PlantRequestPictureEntity, Long> {

    List<PlantRequestPictureEntity> findAllByPlantRequestId(Long id);

}
