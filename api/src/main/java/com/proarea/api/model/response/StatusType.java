package com.proarea.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum StatusType {

    OK(1), ERROR(0);

    private Integer id;

    public static StatusType valueOfIgnoreCase(String name) {
        return Arrays.stream(StatusType.values()).filter(statusType -> statusType.name().equalsIgnoreCase(name)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("Unknown Getter enum name: " + name));
    }

    public static StatusType getById(byte id) {
        return Arrays.stream(StatusType.values()).filter(statusType -> statusType.getId().byteValue() == id).findFirst().orElseThrow(
                () -> new IllegalArgumentException("Unknown Getter enum id: " + id));
    }
}
