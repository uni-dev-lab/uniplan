package org.unilab.uniplan.university;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.university.dto.UniversityRequestDto;

class UniversityMapperTest {

    private final UniversityMapper mapper = new UniversityMapperImpl();

    @Test
    void createEntity_shouldMapAllFields_whenRequestDtoIsValid() {
        final var request = new UniversityRequestDto(
            "Sofia University", "Sofia, Bulgaria", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );

        final var result = mapper.createEntity(request);

        assertThat(result.getUniName()).isEqualTo("Sofia University");
        assertThat(result.getLocation()).isEqualTo("Sofia, Bulgaria");
        assertThat(result.getEstablishedYear()).isEqualTo((short) 1888);
        assertThat(result.getAccreditation()).isEqualTo("NEAA");
        assertThat(result.getWebsite()).isEqualTo("https://uni-sofia.bg");
    }

    @Test
    void createEntity_shouldNotSetId_whenRequestDtoIsProvided() {
        final var request = new UniversityRequestDto(
            "Sofia University", "Sofia, Bulgaria", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );

        final var result = mapper.createEntity(request);

        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenUniversityIsValid() {
        final var university = new University(
            "Sofia University", "Sofia, Bulgaria", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );
        final var id = UUID.randomUUID();
        university.setId(id);

        final var result = mapper.toResponseDto(university);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.uniName()).isEqualTo("Sofia University");
        assertThat(result.location()).isEqualTo("Sofia, Bulgaria");
        assertThat(result.establishedYear()).isEqualTo((short) 1888);
        assertThat(result.accreditation()).isEqualTo("NEAA");
        assertThat(result.website()).isEqualTo("https://uni-sofia.bg");
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        final var university1 = new University(
            "Sofia University", "Sofia", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );
        university1.setId(UUID.randomUUID());

        final var university2 = new University(
            "Plovdiv University", "Plovdiv", (short) 1961, "NEAA", "https://uni-plovdiv.bg"
        );
        university2.setId(UUID.randomUUID());

        final var result = mapper.toResponseDtoList(List.of(university1, university2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).uniName()).isEqualTo("Sofia University");
        assertThat(result.get(1).uniName()).isEqualTo("Plovdiv University");
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final var result = mapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        final var university = new University(
            "Old Name", "Old Location", (short) 1900, "Old Acc", "https://old.bg"
        );
        university.setId(UUID.randomUUID());
        final var request = new UniversityRequestDto(
            "New Name", "New Location", (short) 2000, "New Acc", "https://new.bg"
        );

        mapper.updateEntity(request, university);

        assertThat(university.getUniName()).isEqualTo("New Name");
        assertThat(university.getLocation()).isEqualTo("New Location");
        assertThat(university.getEstablishedYear()).isEqualTo((short) 2000);
        assertThat(university.getAccreditation()).isEqualTo("New Acc");
        assertThat(university.getWebsite()).isEqualTo("https://new.bg");
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final var university = new University(
            "Old Name", "Old Location", (short) 1900, "Old Acc", "https://old.bg"
        );
        final var id = UUID.randomUUID();
        university.setId(id);
        final var request = new UniversityRequestDto(
            "New Name", "New Location", (short) 2000, "New Acc", "https://new.bg"
        );

        mapper.updateEntity(request, university);

        assertThat(university.getId()).isEqualTo(id);
    }
}
