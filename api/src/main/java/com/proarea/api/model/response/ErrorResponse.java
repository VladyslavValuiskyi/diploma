package com.proarea.api.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "An error response")
@Data
public class ErrorResponse {

    @ApiModelProperty(value = "Error status", required = true)
    private Integer status;

    @ApiModelProperty(value = "Error message", required = true)
    private String message;

    @ApiModelProperty(value = "Timestamp in milliseconds", required = true)
    private Long timestamp;

    @ApiModelProperty(value = "API path", required = true)
    private String path;

    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    protected ErrorResponse(final String message, final Integer status, final String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }

    public static ErrorResponse of(final String message, final Integer status, final String path) {
        return new ErrorResponse(message, status, path);
    }

}
