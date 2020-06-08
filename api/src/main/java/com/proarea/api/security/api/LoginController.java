package com.proarea.api.security.api;

import com.proarea.api.security.model.LoginRequest;
import com.proarea.api.security.model.TokenDTO;
import com.proarea.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("${application.api.version.prefix}")
public class LoginController {

    @ApiOperation(value = "Login a user.", response = TokenDTO.class, produces = "application/json")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public TokenDTO login(@RequestBody LoginRequest request) {
        log.info("[LoginController] [login].");
        return null;
    }

}
