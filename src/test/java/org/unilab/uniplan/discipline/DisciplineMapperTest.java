package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

class DisciplineMapperTest {

    private final DisciplineMapper disciplineMapper = new DisciplineMapperImpl();

    @Test
    void toEntity_shouldMapFieldsAndIgnoreIdAndProgramDisciplines_whenCalled() {
        final DisciplineRequestDto request = createRequest();

        final Discipline result = disciplineMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getName()).isEqualTo(request.name()),
            () -> assertThat(result.getMainLector()).isEqualTo(request.mainLector()),
            () -> assertThat(result.getId()).isNull(),
            () -> assertThat(result.getProgramDisciplines()).isEmpty()
        );
    }

    @Test
    void toResponseDto_shouldCopyIdNameAndMainLectorAndProgramDisciplinesMatch_whenCalled() {
        final UUID id = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Discipline discipline = createDiscipline(id, firstLinkId, secondLinkId);

        final DisciplineResponseDto result = disciplineMapper.toResponseDto(discipline);

        assertAll(
            () -> assertThat(result.id()).isEqualTo(id),
            () -> assertThat(result.name()).isEqualTo(discipline.getName()),
            () -> assertThat(result.mainLector()).isEqualTo(discipline.getMainLector()),
            () -> assertThat(result.programDisciplinesIds())
                .containsExactly(firstLinkId, secondLinkId)
        );
    }

    @Test
    void toResponseDto_shouldContainNoProgramDisciplineIds_whenEntityHasNone() {
        final Discipline emptyEntity = new Discipline();

        final DisciplineResponseDto result =
            disciplineMapper.toResponseDto(emptyEntity);

        assertThat(result.programDisciplinesIds()).isEmpty();
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final UUID id = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Discipline discipline = createDiscipline(id, firstLinkId, secondLinkId);
        final DisciplineResponseDto response = new DisciplineResponseDto(
            id,
            discipline.getName(),
            discipline.getMainLector(),
            List.of(firstLinkId, secondLinkId)
        );

        final List<DisciplineResponseDto> result =
            disciplineMapper.toResponseDtoList(List.of(discipline));

        assertThat(result).containsExactly(response);
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<DisciplineResponseDto> result =
            disciplineMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateNameAndLectorAndNotUpdateIdAndProgramDisciplines_whenCalled() {
        final UUID id = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Discipline discipline = createDiscipline(id, firstLinkId, secondLinkId);
        final DisciplineRequestDto request = createRequest();

        disciplineMapper.updateEntityFromDto(request, discipline);

        assertAll(
            () -> assertThat(discipline.getName()).isEqualTo(request.name()),
            () -> assertThat(discipline.getMainLector()).isEqualTo(request.mainLector()),
            () -> assertThat(discipline.getId()).isEqualTo(id),
            () -> assertThat(discipline.getProgramDisciplines()
                                       .stream()
                                       .map(ProgramDiscipline::getId))
                .containsExactly(firstLinkId, secondLinkId)
        );
    }

    private Discipline createDiscipline(final UUID id,
                                        final ProgramDisciplineId... programDisciplineIds) {
        final Discipline discipline = new Discipline();
        discipline.setId(id);
        discipline.setName("Java Spring");
        discipline.setMainLector("Georgi Georgiev");
        discipline.setProgramDisciplines(Arrays.stream(programDisciplineIds)
                                               .map(this::createProgramDiscipline)
                                               .toList());
        return discipline;
    }

    private ProgramDisciplineId createProgramDisciplineId(final UUID disciplineId) {
        return new ProgramDisciplineId(disciplineId, UUID.randomUUID());
    }

    private ProgramDiscipline createProgramDiscipline(final ProgramDisciplineId id) {
        final ProgramDiscipline programDiscipline = new ProgramDiscipline();
        programDiscipline.setId(id);
        return programDiscipline;
    }

    private DisciplineRequestDto createRequest() {
        return new DisciplineRequestDto("Databases", "Petar Petrov");
    }
}