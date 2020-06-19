package com.proarea.api.controller;

import com.proarea.api.Application;
import com.proarea.api.model.request.PlantAddRequest;
import com.proarea.api.model.response.PlantRequestResponse;
import com.proarea.api.service.PlantRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PlantRequestController {

    private final PlantRequestService plantRequestService;

    @Autowired
    public PlantRequestController(PlantRequestService plantRequestService) {
        this.plantRequestService = plantRequestService;
    }

    @ApiOperation(value = "Add plant request", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plant-request/add", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlantRequestResponse addPlantRequest(@RequestBody PlantAddRequest request) {
        return plantRequestService.addPlantRequest(request);
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Accept plant request", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plant-request/accept/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlantRequestResponse acceptPlantRequest(@PathVariable Long id) {
        return plantRequestService.acceptPlantRequest(id);
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Decline plant request", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plant-request/decline/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlantRequestResponse declinePlantRequest(@PathVariable Long id) {
        return plantRequestService.declinePlantRequest(id);
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Accept plant request", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plant-request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlantRequestResponse> getPlantRequests() {
        return plantRequestService.getPlantRequests();
    }
}
