package com.proarea.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantRequestAccept {

    private Long id;

    private String name;

    private String description;

    private String lat;

    private String lng;

    private String createdBy;

    private List<PlantPictureRequest> pictures;
}
