package com.devpies.devpiesback.core.application.domain.model;

import lombok.Builder;

import javax.persistence.Entity;

@Builder
public class Location {
    String latitude;
    String longtitute;
}
