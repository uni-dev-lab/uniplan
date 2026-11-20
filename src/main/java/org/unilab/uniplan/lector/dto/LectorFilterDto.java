package org.unilab.uniplan.lector.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record LectorFilterDto(
    @Size(max = 100)
    String firstName,

    @Size(max = 100)
    String lastName,

    @Size(max = 250)
    String email,

    UUID facultyId,

    @DateTimeFormat(iso = ISO.DATE)
    LocalDate createdFrom,

    @DateTimeFormat(iso = ISO.DATE)
    LocalDate createdTo
) {

}
