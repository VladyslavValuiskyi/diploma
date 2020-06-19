package com.proarea.api.service;

import com.proarea.api.exception.BadRequestException;
import com.proarea.api.model.entity.AuthoritiesEntity;
import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.model.request.RegisterRequest;
import com.proarea.api.model.response.UserInfoResponse;
import com.proarea.api.repository.AuthoritiesRepository;
import com.proarea.api.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    public UserInfoResponse getUserInfo(Authentication authentication, Long id) {
        UserEntity userEntity;
        if (id == null) {
            userEntity = (UserEntity) authentication.getPrincipal();
        } else {
            userEntity = userRepository.findFirstById(id);
        }
        UserInfoResponse response = new UserInfoResponse();

        if (userEntity == null) {
            throw new BadRequestException("User not found");
        }
        response.setName(userEntity.getName());
        response.setEnabled(userEntity.isEnabled());
        response.setId(userEntity.getId());
        response.setUsername(userEntity.getUsername());

        return response;
    }

    public void register(RegisterRequest request) {
        String username = request.getUsername();

        UserEntity user = userRepository.findFirstByUsername(username);

        if (user != null) {
            throw new BadRequestException("User already exist, please choose another username.");
        }

        String password = "y2R1D7gk5x10PadS4EfX" + request.getPassword() + "CuvPf2xQ9B5DPS05uqPP";
        String hash = DigestUtils.md5Hex(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(hash);
        userEntity.setName(request.getName());
        userEntity.setUsername(request.getUsername());
        userEntity.setEnabled(true);
        user = userRepository.save(userEntity);

        authoritiesRepository.save(new AuthoritiesEntity(user.getId(), "ROLE_USER"));
    }

    public void assignModerator(Long userId) {
        UserEntity user = userRepository.findFirstById(userId);

        if (user == null) {
            throw new BadRequestException("User not found");
        }

        List<String> roles = authoritiesRepository.findAllByUserId(userId);
        if (roles.contains("ROLE_ADMIN")) {
            throw new BadRequestException("User is already moderator");
        }

        authoritiesRepository.save(new AuthoritiesEntity(user.getId(), "ROLE_ADMIN"));
    }

    public List<UserInfoResponse> getAllUsers() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(userEntity -> {
            UserInfoResponse response = new UserInfoResponse();
            response.setUsername(userEntity.getUsername());
            response.setId(userEntity.getId());
            response.setEnabled(userEntity.isEnabled());
            response.setName(userEntity.getName());
            return response;
        }).collect(Collectors.toList());
    }

    public UserInfoResponse disableUser(Long id) {
        UserEntity user = userRepository.findFirstById(id);

        if (user == null) {
            throw new BadRequestException("User not found");
        }

        user.setEnabled(false);

        user = userRepository.save(user);

        UserInfoResponse response = new UserInfoResponse();
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setEnabled(user.isEnabled());
        response.setName(user.getName());

        return response;
    }
}
