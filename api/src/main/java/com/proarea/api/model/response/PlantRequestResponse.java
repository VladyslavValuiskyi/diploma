package com.proarea.api.model.response;

import com.proarea.api.model.entity.PlantRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantRequestResponse {

    private PlantRequestEntity plantRequest;

    private List<String> pictureUrls;

}
