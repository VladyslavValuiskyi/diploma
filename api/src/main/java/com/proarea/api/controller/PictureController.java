package com.proarea.api.controller;

import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.model.response.UserInfoResponse;
import com.proarea.api.service.PictureService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.*;

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
