package com.proarea.api.service;

import com.proarea.api.model.entity.*;
import com.proarea.api.model.response.PlantRequestResponse;
import com.proarea.api.model.response.PlantResponse;
import com.proarea.api.repository.PlantPictureRepository;
import com.proarea.api.repository.PlantRepository;
import com.proarea.api.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.proarea.api.util.Constants.PICTURE_URL_PLACEHOLDER;

@Service
public class PlantService {


    private final PlantRepository plantRepository;
    private final PlantPictureRepository plantPictureRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository, PlantPictureRepository plantPictureRepository) {
        this.plantRepository = plantRepository;
        this.plantPictureRepository = plantPictureRepository;
    }

    public List<PlantResponse> getAllPlants() {
        List<PlantResponse> responseList = new ArrayList<>();

        List<PlantEntity> plantEntities = (List<PlantEntity>) plantRepository.findAll();

        Map<String, List<PlantEntity>> typedPlants = new HashMap<>();

        for (PlantEntity entity : plantEntities) {
            if (typedPlants.containsKey(entity.getName())) {
                typedPlants.get(entity.getName()).add(entity);
            } else {
                List<PlantEntity> mapValues = new ArrayList<>();
                mapValues.add(entity);
                typedPlants.put(entity.getName(), mapValues);
            }
        }

        for (Map.Entry<String, List<PlantEntity>> mapEntry : typedPlants.entrySet()) {
            responseList.addAll(convertToArealWithPlants(mapEntry.getValue()));
//            List<PlantPictureEntity> mainPlantPictures = plantPictureRepository.findAllByPlantId(mapEntry.getValue().get(0).getId());
//            PlantResponse response = convertToPlantResponse(mapEntry.getValue().get(0), mainPlantPictures);
//
//            mapEntry.getValue().remove(0);
//
//            for (PlantEntity plantEntity : mapEntry.getValue()) {
//                List<PlantPictureEntity> plantPictures = plantPictureRepository.findAllByPlantId(plantEntity.getId());
//
//                PlantResponse localPlantResponse = convertToPlantResponse(plantEntity, plantPictures);
//                localPlantResponse.setIsAreal(localPlantResponse.isAreal());
//                response.getPlantResponses().add(localPlantResponse);
//
//            }
//
//            response.setIsAreal(response.isAreal());
//            responseList.add(response);
        }

        return responseList;
    }

    public void addPlant(PlantRequestEntity plantRequestEntity, List<Long> pictureIds) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PlantEntity plantEntity = new PlantEntity();
        plantEntity.setName(plantRequestEntity.getName());
        plantEntity.setDescription(plantRequestEntity.getDescription());
        plantEntity.setLat(plantRequestEntity.getLat());
        plantEntity.setLng(plantRequestEntity.getLng());
        plantEntity.setCreatedAt(new Date());
        plantEntity.setCreatedBy(plantRequestEntity.getCreatedBy());
        plantEntity.setAcceptedBy(user.getUsername());

        plantEntity = plantRepository.save(plantEntity);

        final Long plantEntityId = plantEntity.getId();

        List<PlantPictureEntity> plantPictureEntities = pictureIds
                .stream()
                .map(pictureId -> {
                    PlantPictureEntity plantPictureEntity = new PlantPictureEntity();
                    plantPictureEntity.setPictureId(pictureId);
                    plantPictureEntity.setPlantId(plantEntityId);
                    return plantPictureEntity;
                }).collect(Collectors.toList());

        plantPictureRepository.save(plantPictureEntities);

    }

    private PlantResponse convertToPlantResponse(PlantEntity plantEntity, List<PlantPictureEntity> pictures) {
        PlantResponse response = new PlantResponse();
        response.setPlant(plantEntity);
        response.setPictureUrls(convertToPictureUrls(pictures));
        response.setPlantResponses(new ArrayList<>());

        return response;
    }

    private List<String> convertToPictureUrls(List<PlantPictureEntity> entities) {
        return entities.
                stream()
                .map(plantPictureEntity -> PICTURE_URL_PLACEHOLDER + plantPictureEntity.getPictureId())
                .collect(Collectors.toList());
    }

    private List<PlantResponse> convertToArealWithPlants(List<PlantEntity> plantEntities) {
        List<PlantResponse> responseList = new ArrayList<>();
        if (plantEntities.size() == 1) {
            List<PlantPictureEntity> plantPictures = plantPictureRepository.findAllByPlantId(plantEntities.get(0).getId());
            PlantResponse response = convertToPlantResponse(plantEntities.get(0), plantPictures);
            response.setIsAreal(response.isAreal());
            responseList.add(response);
        } else {
//            List<PlantEntity> plantEntitiesCopy = new ArrayList<>(plantEntities);

            List<List<PlantEntity>> arealList = new ArrayList<>();

            outerLoop:
            for (PlantEntity entity : plantEntities) {
                if (arealList.isEmpty()) {
                    arealList.add(new ArrayList<>(Collections.singletonList(entity)));
                    continue;
                }
                for (List<PlantEntity> areal : arealList) {
                    for (PlantEntity plant : areal) {
                        if (MapUtils.isInAreal(
                                MapUtils.getLatLng(plant.getLat(), plant.getLng()),
                                MapUtils.getLatLng(entity.getLat(), entity.getLng()))
                        ) {
                            areal.add(entity);
                            continue outerLoop;
                        }
                    }
                }
                arealList.add(new ArrayList<>(Collections.singletonList(entity)));
            }


            for (List<PlantEntity> areal : arealList) {
                List<PlantPictureEntity> mainPlantPictures = plantPictureRepository.findAllByPlantId(areal.get(0).getId());
                PlantResponse mainPlantResponse = convertToPlantResponse(areal.get(0), mainPlantPictures);
                if (areal.size() > 1) {
                    for (PlantEntity plant : areal) {
                        List<PlantPictureEntity> plantPictures = plantPictureRepository.findAllByPlantId(plant.getId());
                        PlantResponse response = convertToPlantResponse(plant, plantPictures);
                        response.setIsAreal(response.isAreal());
                        mainPlantResponse.getPlantResponses().add(response);
                    }
                }
                mainPlantResponse.setIsAreal(mainPlantResponse.isAreal());
                responseList.add(mainPlantResponse);
            }
        }

        return responseList;
    }
}


//            for (int i = 0; i < plantEntities.size(); i++) {
//                if(responseList.isEmpty()){
//                    List<PlantPictureEntity> plantPictures = plantPictureRepository.findAllByPlantId(plantEntities.get(i).getId());
//                    PlantResponse plantResponse = convertToPlantResponse(plantEntities.get(i), plantPictures);
//                    plantResponse.setIsAreal(plantResponse.isAreal());
//                    responseList.add(plantResponse);
//                }
//                for (int j = 1; j < plantEntities.size(); j++) {
//                    if (!plantEntitiesCopy.isEmpty()) {
//                        MapUtils.isInAreal(
//                                MapUtils.getLatLng(plantEntities.get(i).getLat(), plantEntities.get(i).getLng()),
//                                MapUtils.getLatLng(plantEntities.get(j).getLat(), plantEntities.get(j).getLng())
//                        );
//                    }
//                }
//
//            }