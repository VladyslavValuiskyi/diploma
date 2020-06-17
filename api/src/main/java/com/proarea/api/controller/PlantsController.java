package com.proarea.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlantsController {

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Add plant", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/plants/add", method = RequestMethod.GET)
    public String addPlant() {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "some str";
    }

}
