package com.proarea.api.service;

import com.proarea.api.model.entity.PictureEntity;
import com.proarea.api.repository.PictureRepository;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;

@Service
public class PictureService {

    private final PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public byte[] getPictureById(Long id) {
        PictureEntity entity = pictureRepository.findFirstById(id);

        return entity.getDecodedBase();
    }

}
