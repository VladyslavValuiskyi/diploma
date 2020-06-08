package com.proarea.api.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class StatusResponse {

    @ApiModelProperty(value = "Status", required = true)
    private StatusType status;

    @ApiModelProperty(value = "Status message", required = true)
    private String message;

    public static StatusResponse success() {
        return new StatusResponse(StatusType.OK, "");
    }

    public static StatusResponse success(String message) {
        return new StatusResponse(StatusType.OK, message);
    }

    public static StatusResponse failure(String message) {
        return new StatusResponse(StatusType.ERROR, message);
    }
}
