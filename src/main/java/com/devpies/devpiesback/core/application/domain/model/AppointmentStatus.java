package com.devpies.devpiesback.core.application.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum AppointmentStatus {
    ACTIVE("CODE_1"),
    RESOLVED("CODE_2"),
    PENDING("CODE_3"),
    REJECTED("CODE_4"),
    CANCELLED("CODE_5");

    private String code;

    AppointmentStatus(String code) {
        this.code = code;
    }

    @JsonCreator
    public static AppointmentStatus decode(final String code) {
        return Stream.of(AppointmentStatus.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

}
