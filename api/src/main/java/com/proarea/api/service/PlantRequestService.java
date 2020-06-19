package com.proarea.api.service;

import com.proarea.api.exception.BadRequestException;
import com.proarea.api.model.entity.*;
import com.proarea.api.model.request.PlantAddRequest;
import com.proarea.api.model.request.PlantPictureRequest;
import com.proarea.api.model.request.PlantRequestAccept;
import com.proarea.api.model.response.PlantRequestResponse;
import com.proarea.api.repository.PictureRepository;
import com.proarea.api.repository.PlantRepository;
import com.proarea.api.repository.PlantRequestPictureRepository;
import com.proarea.api.repository.PlantRequestRepository;
import com.proarea.api.util.enums.PlantRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.proarea.api.util.Constants.PICTURE_URL_PLACEHOLDER;

@Service
public class PlantRequestService {

    private final PlantRequestRepository plantRequestRepository;
    private final PlantRequestPictureRepository plantRequestPictureRepository;
    private final PlantRepository plantRepository;
    private final PictureRepository pictureRepository;
    private final PlantService plantService;

    @Autowired
    public PlantRequestService(PlantRequestRepository plantRequestRepository, PlantRequestPictureRepository plantRequestPictureRepository, PlantRepository plantRepository, PictureRepository pictureRepository, PlantService plantService) {
        this.plantRequestRepository = plantRequestRepository;
        this.plantRequestPictureRepository = plantRequestPictureRepository;
        this.plantRepository = plantRepository;
        this.pictureRepository = pictureRepository;
        this.plantService = plantService;
    }

    public PlantRequestResponse addPlantRequest(PlantAddRequest request) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        PlantEntity plantEntity = plantRepository.findFirstByName(plantName);
//
//        if (plantEntity != null) {
//            throw new BadRequestException("Plant already exist.");
//        }

        PlantRequestEntity plantRequestEntity = new PlantRequestEntity();
        plantRequestEntity.setName(request.getName());
        plantRequestEntity.setDescription(request.getDescription());
        plantRequestEntity.setLat(request.getLat());
        plantRequestEntity.setLng(request.getLng());
        plantRequestEntity.setStatus(PlantRequestStatus.CREATED.getStatus());
        plantRequestEntity.setCreatedAt(new Date());
        plantRequestEntity.setCreatedBy(user.getUsername());

        plantRequestEntity = plantRequestRepository.save(plantRequestEntity);

        List<PictureEntity> pictures = new ArrayList<>();

        for (PlantPictureRequest plantPictureRequest : request.getPictures()) {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setBase64(plantPictureRequest.getBase64());
            pictureEntity.setCreatedAt(new Date());
            pictures.add(pictureEntity);
        }

        pictures = (List<PictureEntity>) pictureRepository.save(pictures);

        List<PlantRequestPictureEntity> requestPictures = new ArrayList<>();
        for (PictureEntity picture : pictures) {
            PlantRequestPictureEntity entity = new PlantRequestPictureEntity();
            entity.setPictureId(picture.getId());
            entity.setPlantRequestId(plantRequestEntity.getId());
            requestPictures.add(entity);
        }

        requestPictures = (List<PlantRequestPictureEntity>) plantRequestPictureRepository.save(requestPictures);


        return convertToPlantRequestResponse(plantRequestEntity, requestPictures);
    }

    public List<PlantRequestResponse> getPlantRequests() {
        List<PlantRequestResponse> resultList = new ArrayList<>();
        List<PlantRequestEntity> requests = plantRequestRepository.findAllByStatus(PlantRequestStatus.CREATED.getStatus());
        for (PlantRequestEntity entity : requests) {
            List<PlantRequestPictureEntity> pictureEntities = plantRequestPictureRepository.findAllByPlantRequestId(entity.getId());
            resultList.add(convertToPlantRequestResponse(entity, pictureEntities));
        }

        return resultList;
    }

    public PlantRequestResponse declinePlantRequest(Long id) {
        PlantRequestEntity plantRequestEntity = plantRequestRepository.findFirstById(id);

        plantRequestEntity.setStatus(PlantRequestStatus.DECLINED.getStatus());

        plantRequestEntity = plantRequestRepository.save(plantRequestEntity);
        List<PlantRequestPictureEntity> pictureEntities = plantRequestPictureRepository.findAllByPlantRequestId(plantRequestEntity.getId());

        return convertToPlantRequestResponse(plantRequestEntity, pictureEntities);
    }

    public PlantRequestResponse acceptPlantRequest(Long id, PlantRequestAccept plantRequestAccept) {

        if (plantRequestAccept != null && plantRequestAccept.getId() != null) {
            PlantRequestEntity entity = plantRequestRepository.findFirstById(plantRequestAccept.getId());

            if (entity == null) {
                throw new BadRequestException("Plant request not found");
            }

            if(!plantRequestAccept.getId().equals(id)){
                throw new BadRequestException("Incorrect Id in path or in request body");
            }

            entity.setName(plantRequestAccept.getName());
            entity.setDescription(plantRequestAccept.getDescription());
            entity.setCreatedBy(plantRequestAccept.getCreatedBy());
            entity.setLng(plantRequestAccept.getLng());
            entity.setLat(plantRequestAccept.getLat());

            plantRequestRepository.save(entity);

        }

        PlantRequestEntity plantRequestEntity = plantRequestRepository.findFirstById(id);

        if (plantRequestEntity.getName() == null || plantRequestEntity.getName().equals("")) {
            throw new BadRequestException("Plant name shouldn't be empty");
        }

        plantRequestEntity.setStatus(PlantRequestStatus.ACCEPTED.getStatus());
        plantRequestEntity = plantRequestRepository.save(plantRequestEntity);
        List<PlantRequestPictureEntity> pictureEntities = plantRequestPictureRepository.findAllByPlantRequestId(plantRequestEntity.getId());

        List<Long> pictureIds = pictureEntities
                .stream()
                .map(PlantRequestPictureEntity::getPictureId)
                .collect(Collectors.toList());

        plantService.addPlant(plantRequestEntity, pictureIds);

        return convertToPlantRequestResponse(plantRequestEntity, pictureEntities);
    }

    private PlantRequestResponse convertToPlantRequestResponse(PlantRequestEntity plantRequestEntity, List<PlantRequestPictureEntity> pictures) {
        PlantRequestResponse response = new PlantRequestResponse();
        response.setPlantRequest(plantRequestEntity);
        response.setPictureUrls(pictures.
                stream()
                .map(plantRequestPictureEntity -> PICTURE_URL_PLACEHOLDER + plantRequestPictureEntity.getPictureId())
                .collect(Collectors.toList()));

        return response;
    }
}
