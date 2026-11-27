package org.unilab.uniplan.building;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.building.dto.BuildingResponseDto;
import org.unilab.uniplan.university.University;

class BuildingMapperTest {

    private final BuildingMapper buildingMapper = new BuildingMapperImpl();

    @Test
    void toEntity_shouldMapFieldsAndIgnoreRelations_whenRequestIsValid() {
        final BuildingRequestDto request = new BuildingRequestDto(
            "Main Building",
            "123 Campus Road",
            UUID.randomUUID()
        );

        final Building result = buildingMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getName()).isEqualTo(request.name()),
            () -> assertThat(result.getAddress()).isEqualTo(request.address()),
            () -> assertThat(result.getId()).isNull(),
            () -> assertThat(result.getUniversity()).isNull(),
            () -> assertThat(result.getRooms()).isEmpty()
        );
    }

    @Test
    void toResponseDto_shouldMapUniversityDetails_whenBuildingHasUniversity() {
        final UUID buildingId = UUID.randomUUID();
        final UUID universityId = UUID.randomUUID();

        final University university = new University();
        ReflectionTestUtils.setField(university, "id", universityId);
        ReflectionTestUtils.setField(university, "uniName", "Test University");

        final Building building = new Building();
        ReflectionTestUtils.setField(building, "id", buildingId);
        building.setName("Main Building");
        building.setAddress("123 Campus Road");
        building.setUniversity(university);

        final BuildingResponseDto result = buildingMapper.toResponseDto(building);

        assertAll(
            () -> assertThat(result.id()).isEqualTo(buildingId),
            () -> assertThat(result.name()).isEqualTo("Main Building"),
            () -> assertThat(result.address()).isEqualTo("123 Campus Road"),
            () -> assertThat(result.universityId()).isEqualTo(universityId),
            () -> assertThat(result.universityName()).isEqualTo("Test University")
        );
    }

    @Test
    void toResponseDto_shouldLeaveUniversityFieldsNull_whenBuildingHasNoUniversity() {
        final Building building = new Building();
        building.setName("Main Building");
        building.setAddress("Campus Road");

        final BuildingResponseDto result = buildingMapper.toResponseDto(building);

        assertAll(
            () -> assertThat(result.name()).isEqualTo("Main Building"),
            () -> assertThat(result.address()).isEqualTo("Campus Road"),
            () -> assertThat(result.universityId()).isNull(),
            () -> assertThat(result.universityName()).isNull()
        );
    }

    @Test
    void toResponseDtoList_shouldMapAllBuildings_whenBuildingsExist() {
        final Building firstBuilding = new Building();
        firstBuilding.setName("Building A");
        firstBuilding.setAddress("Address A");

        final Building secondBuilding = new Building();
        secondBuilding.setName("Building B");
        secondBuilding.setAddress("Address B");

        final List<BuildingResponseDto> result =
            buildingMapper.toResponseDtoList(
                List.of(firstBuilding, secondBuilding)
            );

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result)
                .extracting(BuildingResponseDto::name)
                .containsExactly("Building A", "Building B"),
            () -> assertThat(result)
                .extracting(BuildingResponseDto::address)
                .containsExactly("Address A", "Address B")
        );
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<BuildingResponseDto> result = buildingMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateFieldsAndPreserveRelations_whenRequestIsValid() {
        final UUID buildingId = UUID.randomUUID();
        final University university = new University();

        final Building building = new Building();
        ReflectionTestUtils.setField(building, "id", buildingId);
        building.setName("Old Building");
        building.setAddress("Old Address");
        building.setUniversity(university);

        final var originalRooms = building.getRooms();

        final BuildingRequestDto request = new BuildingRequestDto(
            "Updated Building",
            "Updated Address",
            UUID.randomUUID()
        );

        buildingMapper.updateEntityFromDto(request, building);

        assertAll(
            () -> assertThat(building.getName()).isEqualTo(request.name()),
            () -> assertThat(building.getAddress()).isEqualTo(request.address()),
            () -> assertThat(building.getId()).isEqualTo(buildingId),
            () -> assertThat(building.getUniversity()).isSameAs(university),
            () -> assertThat(building.getRooms()).isSameAs(originalRooms)
        );
    }
}
