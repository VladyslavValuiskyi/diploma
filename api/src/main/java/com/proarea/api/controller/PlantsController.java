package com.proarea.api.controller;

import com.proarea.api.model.entity.PlantEntity;
import com.proarea.api.service.PlantService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PlantsController {

    private final PlantService plantService;

    @Autowired
    public PlantsController(PlantService plantService) {
        this.plantService = plantService;
    }
//
//    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Get plants", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plants", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlantEntity> getPlants() {
        return plantService.getAllPlants();
    }

}
