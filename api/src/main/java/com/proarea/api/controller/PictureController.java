package com.proarea.api.controller;

import com.proarea.api.service.PictureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @ApiOperation(value = "Get image by id")//todo uncomment, authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/pictures/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getPictureById(@PathVariable Long id) {
        return pictureService.getPictureById(id);
    }
}
