package org.unilab.uniplan.building.dto;

import java.util.UUID;

public record BuildingResponseDto (
    UUID id,
    String name,
    String address,
    UUID universityId,
    String universityName
){}
