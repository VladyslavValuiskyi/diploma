package com.proarea.api.repository;

import com.proarea.api.model.entity.PictureEntity;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<PictureEntity, Long> {

    PictureEntity findFirstById(Long id);

}
