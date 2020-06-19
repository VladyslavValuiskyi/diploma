package com.proarea.api.util.enums;

import lombok.Getter;

@Getter
public enum PlantRequestStatus {

    CREATED("created"),
    ACCEPTED("accepted"),
    DECLINED("declined");

    PlantRequestStatus(String status){
        this.status = status;
    }

    private final String status;

}
