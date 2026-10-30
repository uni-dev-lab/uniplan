package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

class ProgramDisciplineLectorMapperTest {

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper =
        new ProgramDisciplineLectorMapperImpl();

    @Test
    void toEntity_shouldBuildCompositeIdAndIgnoreRelationships_whenCalled() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLectorRequestDto request = createRequest(id, LectorType.BOTH);

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
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLector entity = createEntity(id, LectorType.LECTURE);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorMapper.toResponseDto(entity);

        assertThat(result).isEqualTo(createResponse(id, LectorType.LECTURE));
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final ProgramDisciplineLectorId firstId = createId();
        final ProgramDisciplineLectorId secondId = createId();
        final List<ProgramDisciplineLector> entities = List.of(
            createEntity(firstId, LectorType.LECTURE),
            createEntity(secondId, LectorType.EXERCISE));

        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorMapper.toResponseDtoList(entities);

        assertThat(result).containsExactly(createResponse(firstId, LectorType.LECTURE),
                                           createResponse(secondId, LectorType.EXERCISE));
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateLectorTypeAndKeepIdAndRelationships_whenRequestHasOtherIds() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLector entity = createEntity(id, LectorType.LECTURE);
        final ProgramDisciplineLectorRequestDto request =
            createRequest(createId(), LectorType.EXERCISE);

        programDisciplineLectorMapper.updateEntityFromDto(request, entity);

        assertAll(
            () -> assertThat(entity.getLectorType()).isEqualTo(request.lectorType()),
            () -> assertThat(entity.getId()).isEqualTo(id),
            () -> assertThat(entity.getLector()).isNull(),
            () -> assertThat(entity.getProgram()).isNull(),
            () -> assertThat(entity.getDiscipline()).isNull()
        );
    }

    private ProgramDisciplineLectorId createId() {
        return new ProgramDisciplineLectorId(UUID.randomUUID(),
                                             UUID.randomUUID(),
                                             UUID.randomUUID());
    }

    private ProgramDisciplineLector createEntity(final ProgramDisciplineLectorId id,
                                                 final LectorType lectorType) {
        final ProgramDisciplineLector entity = new ProgramDisciplineLector();
        entity.setId(id);
        entity.setLectorType(lectorType);
        return entity;
    }

    private ProgramDisciplineLectorRequestDto createRequest(final ProgramDisciplineLectorId id,
                                                            final LectorType lectorType) {
        return new ProgramDisciplineLectorRequestDto(id.getLectorId(),
                                                     id.getProgramId(),
                                                     id.getDisciplineId(),
                                                     lectorType);
    }

    private ProgramDisciplineLectorResponseDto createResponse(final ProgramDisciplineLectorId id,
                                                              final LectorType lectorType) {
        return new ProgramDisciplineLectorResponseDto(id,
                                                      id.getLectorId(),
                                                      id.getProgramId(),
                                                      id.getDisciplineId(),
                                                      lectorType);
    }
}