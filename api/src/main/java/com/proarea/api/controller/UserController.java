package com.proarea.api.controller;

import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.model.request.RegisterRequest;
import com.proarea.api.model.response.UserInfoResponse;
import com.proarea.api.repository.UserRepository;
import com.proarea.api.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get user info", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/user/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInfoResponse getUserInfo(Authentication authentication, @RequestParam(required = false) Long id) {
        return userService.getUserInfo(authentication, id);
    }

    @ApiOperation(value = "register user")
    @RequestMapping(value = "/user/registration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {

        userService.register(request);

        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "assign user to moderator", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/user/assign-moderator/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignModerator(@PathVariable Long userId) {
        userService.assignModerator(userId);

        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "get all users", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/user/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserInfoResponse> getAllUsers(){
        return userService.getAllUsers(true);
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "get all users", authorizations = @Authorization("Authorization"))
    @RequestMapping(value = "/user/disable/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInfoResponse disableUser(@PathVariable Long id){
        return userService.disableUser(id);
    }

}
