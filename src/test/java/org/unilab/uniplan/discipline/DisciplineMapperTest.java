package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

class DisciplineMapperTest {

    private final DisciplineMapper disciplineMapper = new DisciplineMapperImpl();

    private UUID id;
    private ProgramDisciplineId firstLinkId;
    private ProgramDisciplineId secondLinkId;
    private Discipline discipline;
    private DisciplineRequestDto request;
    private DisciplineResponseDto response;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        firstLinkId = new ProgramDisciplineId(id, UUID.randomUUID());
        secondLinkId = new ProgramDisciplineId(id, UUID.randomUUID());

        final ProgramDiscipline firstLink = new ProgramDiscipline();
        firstLink.setId(firstLinkId);

        final ProgramDiscipline secondLink = new ProgramDiscipline();
        secondLink.setId(secondLinkId);

        discipline = new Discipline();
        discipline.setId(id);
        discipline.setName("Java Spring");
        discipline.setMainLector("Georgi Georgiev");
        discipline.getProgramDisciplines().add(firstLink);
        discipline.getProgramDisciplines().add(secondLink);

        request = new DisciplineRequestDto(
            "Databases",
            "Petar Petrov",
            List.of(new ProgramDisciplineId(UUID.randomUUID(), UUID.randomUUID())));

        response = new DisciplineResponseDto(
            id,
            "Java Spring",
            "Georgi Georgiev",
            List.of(firstLinkId, secondLinkId)
        );
    }

    @Test
    void toEntity_shouldCopyNameAndLector_whenCalled() {
        final Discipline result = disciplineMapper.toEntity(request);

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getMainLector()).isEqualTo(request.mainLector());
    }

    @Test
    void toEntity_shouldIgnoreIdAndProgramDisciplines_whenCalled() {
        final Discipline result = disciplineMapper.toEntity(request);

        assertThat(result.getId()).isNull();
        assertThat(result.getProgramDisciplines()).isEmpty();
    }

    @Test
    void toResponseDto_shouldCopyIdNameAndMainLector_whenCalled() {
        final DisciplineResponseDto result =
            disciplineMapper.toResponseDto(discipline);

        assertThat(result.id()).isEqualTo(discipline.getId());
        assertThat(result.name()).isEqualTo(discipline.getName());
        assertThat(result.mainLector()).isEqualTo(discipline.getMainLector());
    }

    @Test
    void toResponseDto_shouldContainOnlyProgramDisciplineIdsEntityHas_whenCalled() {
        final DisciplineResponseDto result =
            disciplineMapper.toResponseDto(discipline);

        assertThat(result.programDisciplinesIds())
            .containsExactly(firstLinkId, secondLinkId);
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
    void updateEntityFromDto_shouldUpdateNameAndLector_whenCalled() {
        disciplineMapper.updateEntityFromDto(request, discipline);

        assertThat(discipline.getName()).isEqualTo(request.name());
        assertThat(discipline.getMainLector()).isEqualTo(request.mainLector());
    }

    @Test
    void updateEntityFromDto_shouldNotUpdateIdAndProgramDisciplines_whenCalled() {
        disciplineMapper.updateEntityFromDto(request, discipline);

        assertThat(discipline.getId()).isEqualTo(id);
        assertThat(discipline.getProgramDisciplines()
                             .stream()
                             .map(ProgramDiscipline::getId))
            .containsExactly(firstLinkId, secondLinkId);
    }
}
