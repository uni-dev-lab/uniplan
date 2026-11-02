package org.unilab.uniplan.programdiscipline;

import org.junit.jupiter.api.Test;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProgramDisciplineMapperTest {

    private final ProgramDisciplineMapper programDisciplineMapper =
        new ProgramDisciplineMapperImpl();

    @Test
    void toEntity_shouldBuildCompositeIdAndIgnoreRelationships_whenCalled() {
        final ProgramDisciplineId id = createId();
        final ProgramDisciplineRequestDto request = createRequest(id);

        final ProgramDiscipline result = programDisciplineMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getId()).isEqualTo(id),
            () -> assertThat(result.getHoursLecture()).isEqualTo(request.hoursLecture()),
            () -> assertThat(result.getHoursExercises()).isEqualTo(request.hoursExercises()),
            () -> assertThat(result.getSemesterCount()).isEqualTo(request.semesterCount()),
            () -> assertThat(result.getProgram()).isNull(),
            () -> assertThat(result.getDiscipline()).isNull()
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenCalled() {
        final ProgramDisciplineId id = createId();
        final ProgramDiscipline entity = createEntity(id);

        final ProgramDisciplineResponseDto result =
            programDisciplineMapper.toResponseDto(entity);

        assertThat(result).isEqualTo(createResponse(id));
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final ProgramDisciplineId firstId = createId();
        final ProgramDisciplineId secondId = createId();
        final List<ProgramDiscipline> entities = List.of(
            createEntity(firstId),
            createEntity(secondId));

        final List<ProgramDisciplineResponseDto> result =
            programDisciplineMapper.toResponseDtoList(entities);

        assertThat(result).containsExactly(
            createResponse(firstId),
            createResponse(secondId));
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<ProgramDisciplineResponseDto> result =
            programDisciplineMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateHoursAndSemesterAndKeepIdAndRelationships_whenRequestHasOtherIds() {
        final ProgramDisciplineId id = createId();
        final ProgramDiscipline entity = createEntity(id);
        final ProgramDisciplineRequestDto request =
            new ProgramDisciplineRequestDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                (short) 2,
                (short) 3,
                (byte) 4);

        programDisciplineMapper.updateEntityFromDto(request, entity);

        assertAll(
            () -> assertThat(entity.getHoursLecture()).isEqualTo(request.hoursLecture()),
            () -> assertThat(entity.getHoursExercises()).isEqualTo(request.hoursExercises()),
            () -> assertThat(entity.getSemesterCount()).isEqualTo(request.semesterCount()),
            () -> assertThat(entity.getId()).isEqualTo(id),
            () -> assertThat(entity.getProgram()).isNull(),
            () -> assertThat(entity.getDiscipline()).isNull()
        );
    }

    private ProgramDisciplineId createId() {
        return new ProgramDisciplineId(
            UUID.randomUUID(), UUID.randomUUID());
    }

    private ProgramDiscipline createEntity(
        final ProgramDisciplineId id) {

        final ProgramDiscipline entity = new ProgramDiscipline();
        entity.setId(id);
        entity.setHoursLecture((short) 1);
        entity.setHoursExercises((short) 1);
        entity.setSemesterCount((byte) 1);
        return entity;
    }

    private ProgramDisciplineRequestDto createRequest(
        final ProgramDisciplineId id) {

        return new ProgramDisciplineRequestDto(
            id.getDisciplineId(),
            id.getProgramId(),
            (short) 1,
            (short) 1,
            (byte) 1);
    }

    private ProgramDisciplineResponseDto createResponse(
        final ProgramDisciplineId id) {

        return new ProgramDisciplineResponseDto(
            id,
            id.getDisciplineId(),
            id.getProgramId(),
            (short) 1,
            (short) 1,
            (byte) 1
        );
    }
}
