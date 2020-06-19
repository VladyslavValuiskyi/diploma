package com.proarea.api.model.response;

import com.proarea.api.model.entity.PlantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantResponse {

    private PlantEntity plant;

    private List<String> pictureUrls;

    private Boolean isAreal;

    private List<PlantResponse> plantResponses;

    public boolean isAreal() {
        return plantResponses != null && plantResponses.size() > 0;
    }

}
