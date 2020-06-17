package com.proarea.api.service;

import com.proarea.api.exception.BadRequestException;
import com.proarea.api.model.entity.AuthoritiesEntity;
import com.proarea.api.model.entity.UserEntity;
import com.proarea.api.model.request.RegisterRequest;
import com.proarea.api.repository.AuthoritiesRepository;
import com.proarea.api.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
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

}
