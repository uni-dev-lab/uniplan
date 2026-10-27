package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

class ProgramDisciplineLectorMapperTest {

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper =
        new ProgramDisciplineLectorMapperImpl();

    private UUID lectorId;
    private UUID programId;
    private UUID disciplineId;
    private ProgramDisciplineLectorId id;
    private ProgramDisciplineLector entity;
    private ProgramDisciplineLectorRequestDto request;
    private ProgramDisciplineLectorRequestDto requestWithOtherIds;
    private ProgramDisciplineLectorResponseDto response;

    @BeforeEach
    void setUp() {
        lectorId = UUID.randomUUID();
        programId = UUID.randomUUID();
        disciplineId = UUID.randomUUID();
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
    void toEntity_shouldBuildCompositeIdAndCopyLectorType_whenCalled() {
        final ProgramDisciplineLector result = programDisciplineLectorMapper.toEntity(request);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getLectorType()).isEqualTo(request.lectorType());
    }

    @Test
    void toEntity_shouldIgnoreRelationships_whenCalled() {
        final ProgramDisciplineLector result = programDisciplineLectorMapper.toEntity(request);

        assertThat(result.getLector()).isNull();
        assertThat(result.getProgram()).isNull();
        assertThat(result.getDiscipline()).isNull();
    }

    @Test
    void toResponseDto_shouldReadIdsFromEntityIdAndCopyIdAndLectorType_whenCalled() {
        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorMapper.toResponseDto(entity);

        assertThat(result.lectorId()).isEqualTo(lectorId);
        assertThat(result.programId()).isEqualTo(programId);
        assertThat(result.disciplineId()).isEqualTo(disciplineId);
        assertThat(result.id()).isEqualTo(entity.getId());
        assertThat(result.lectorType()).isEqualTo(entity.getLectorType());
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
    void updateEntityFromDto_shouldUpdateLectorType_whenCalled() {
        programDisciplineLectorMapper.updateEntityFromDto(request, entity);

        assertThat(entity.getLectorType()).isEqualTo(LectorType.BOTH);
    }

    @Test
    void updateEntityFromDto_shouldKeepIdAndRelationships_whenCalled() {
        programDisciplineLectorMapper.updateEntityFromDto(requestWithOtherIds, entity);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getLector()).isNull();
        assertThat(entity.getProgram()).isNull();
        assertThat(entity.getDiscipline()).isNull();
    }
}