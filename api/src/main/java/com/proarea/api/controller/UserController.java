package com.proarea.api.controller;

import com.proarea.api.model.request.RegisterRequest;
import com.proarea.api.repository.UserRepository;
import com.proarea.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get user info", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getUserInfo() {
    }

    @ApiOperation(value = "register user ")
    @RequestMapping(value = "/user/registration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {

        userService.register(request);

        return ResponseEntity.noContent().build();
    }
}
