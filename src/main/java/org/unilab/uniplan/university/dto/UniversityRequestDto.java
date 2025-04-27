package org.unilab.uniplan.university.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UniversityRequestDto(

    @NotNull(message = "University name cannot be null")
    @Size(min = 1, max = 200, message = "University name must be between 1 and 200 characters")
    String uniName,

    @NotNull(message = "Location cannot be null")
    @Size(max = 500, message = "Location must be less than or equal to 500 characters")
    String location,

    @Positive
    short establishedYear,

    @NotNull(message = "Accreditation cannot be null")
    @Size(max = 200, message = "Accreditation must be less than or equal to 200 characters")
    String accreditation,

    @NotNull(message = "Website cannot be null")
    @Size(max = 2048, message = "Website must be less than or equal to 2048 characters")
    String website
) {

}
