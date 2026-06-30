package org.unilab.uniplan.university;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

class UniversityMapperTest {

    private final UniversityMapper mapper = new UniversityMapperImpl();
    private UniversityRequestDto request;
    private University university;
    private University university2;
    private UUID id;
    private UniversityRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        request = new UniversityRequestDto(
            "Sofia University", "Sofia, Bulgaria", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );
        updateRequest = new UniversityRequestDto(
            "New Name", "New Location", (short) 2000, "New Acc", "https://new.bg"
        );
        university = new University(
            "Sofia University", "Sofia, Bulgaria", (short) 1888, "NEAA", "https://uni-sofia.bg"
        );
        university2 = new University(
            "Plovdiv University", "Plovdiv", (short) 1961, "NEAA", "https://uni-plovdiv.bg"
        );
        id = UUID.randomUUID();
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {

        final University result = mapper.toEntity(request);

        assertThat(result.getUniName()).isEqualTo("Sofia University");
        assertThat(result.getLocation()).isEqualTo("Sofia, Bulgaria");
        assertThat(result.getEstablishedYear()).isEqualTo((short) 1888);
        assertThat(result.getAccreditation()).isEqualTo("NEAA");
        assertThat(result.getWebsite()).isEqualTo("https://uni-sofia.bg");
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenUniversityIsValid() {
        university.setId(id);

        final UniversityResponseDto result = mapper.toResponseDto(university);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.uniName()).isEqualTo("Sofia University");
        assertThat(result.location()).isEqualTo("Sofia, Bulgaria");
        assertThat(result.establishedYear()).isEqualTo((short) 1888);
        assertThat(result.accreditation()).isEqualTo("NEAA");
        assertThat(result.website()).isEqualTo("https://uni-sofia.bg");
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        university.setId(UUID.randomUUID());
        university2.setId(UUID.randomUUID());

        final List<UniversityResponseDto> result = mapper.toResponseDtoList(List.of(university,
                                                                                    university2));

        assertThat(result)
            .hasSize(2)
            .extracting(UniversityResponseDto::uniName)
            .containsExactly("Sofia University", "Plovdiv University");
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final List<UniversityResponseDto> result = mapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        university.setId(id);

        mapper.updateEntity(updateRequest, university);

        assertThat(university.getUniName()).isEqualTo("New Name");
        assertThat(university.getLocation()).isEqualTo("New Location");
        assertThat(university.getEstablishedYear()).isEqualTo((short) 2000);
        assertThat(university.getAccreditation()).isEqualTo("New Acc");
        assertThat(university.getWebsite()).isEqualTo("https://new.bg");
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        university.setId(id);

        mapper.updateEntity(updateRequest, university);

        assertThat(university.getId()).isEqualTo(id);
    }
}
