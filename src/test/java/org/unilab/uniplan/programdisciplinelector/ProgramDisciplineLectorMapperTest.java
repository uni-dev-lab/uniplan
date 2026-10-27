package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

class ProgramDisciplineLectorMapperTest {

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper =
        new ProgramDisciplineLectorMapperImpl();

    private ProgramDisciplineLectorId id;
    private ProgramDisciplineLector entity;
    private ProgramDisciplineLectorRequestDto request;
    private ProgramDisciplineLectorRequestDto requestWithOtherIds;
    private ProgramDisciplineLectorResponseDto response;

    @BeforeEach
    void setUp() {
        final UUID lectorId = UUID.randomUUID();
        final UUID programId = UUID.randomUUID();
        final UUID disciplineId = UUID.randomUUID();

        id = new ProgramDisciplineLectorId(lectorId, programId, disciplineId);

        entity = new ProgramDisciplineLector();
        entity.setId(id);
        entity.setLectorType(LectorType.LECTURE);

        request = new ProgramDisciplineLectorRequestDto(
            lectorId,
            programId,
            disciplineId,
            LectorType.BOTH);

        requestWithOtherIds = new ProgramDisciplineLectorRequestDto(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            LectorType.EXERCISE);

        response = new ProgramDisciplineLectorResponseDto(
            id,
            lectorId,
            programId,
            disciplineId,
            LectorType.LECTURE);
    }

    @Test
    void toEntity_shouldBuildCompositeIdAndIgnoreRelationships_whenCalled() {
        final ProgramDisciplineLector result = programDisciplineLectorMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getId()).isEqualTo(id),
            () -> assertThat(result.getLectorType()).isEqualTo(request.lectorType()),
            () -> assertThat(result.getLector()).isNull(),
            () -> assertThat(result.getProgram()).isNull(),
            () -> assertThat(result.getDiscipline()).isNull()
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenCalled() {
        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorMapper.toResponseDto(entity);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorMapper.toResponseDtoList(List.of(entity));

        assertThat(result).containsExactly(response);
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateLectorTypeAndKeepIdAndRelationships_whenRequestHasOtherIds() {
        programDisciplineLectorMapper.updateEntityFromDto(requestWithOtherIds, entity);

        assertAll(
            () -> assertThat(entity.getLectorType()).isEqualTo(LectorType.EXERCISE),
            () -> assertThat(entity.getId()).isEqualTo(id),
            () -> assertThat(entity.getLector()).isNull(),
            () -> assertThat(entity.getProgram()).isNull(),
            () -> assertThat(entity.getDiscipline()).isNull()
        );
    }
}