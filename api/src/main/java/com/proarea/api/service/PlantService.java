package com.proarea.api.service;

import com.proarea.api.model.entity.PlantEntity;
import com.proarea.api.model.entity.PlantPictureEntity;
import com.proarea.api.model.entity.PlantRequestEntity;
import com.proarea.api.repository.PlantPictureRepository;
import com.proarea.api.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final PlantPictureRepository plantPictureRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository, PlantPictureRepository plantPictureRepository) {
        this.plantRepository = plantRepository;
        this.plantPictureRepository = plantPictureRepository;
    }

    public List<PlantEntity> getAllPlants() {
        return (List<PlantEntity>) plantRepository.findAll();
    }

    public void addPlant(PlantRequestEntity plantRequestEntity, List<Long> pictureIds){
        PlantEntity plantEntity = new PlantEntity();
        plantEntity.setName(plantRequestEntity.getName());
        plantEntity.setDescription(plantRequestEntity.getDescription());
        plantEntity.setLat(plantRequestEntity.getLat());
        plantEntity.setLng(plantRequestEntity.getLng());
        plantEntity.setCreatedAt(new Date());

        plantEntity = plantRepository.save(plantEntity);

        final Long plantEntityId = plantEntity.getId();

        List<PlantPictureEntity> plantPictureEntities = pictureIds
                .stream()
                .map((Function<Long, PlantPictureEntity>) pictureId -> {
                    PlantPictureEntity plantPictureEntity = new PlantPictureEntity();
                    plantPictureEntity.setPictureId(pictureId);
                    plantPictureEntity.setPlantId(plantEntityId);
                    return null;
                }).collect(Collectors.toList());

        plantPictureRepository.save(plantPictureEntities);

    }

}
