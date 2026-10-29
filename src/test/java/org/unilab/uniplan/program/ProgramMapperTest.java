package org.unilab.uniplan.program;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

class ProgramMapperTest {

    private final ProgramMapper programMapper = new ProgramMapperImpl();

    private UUID id;
    private UUID courseId;
    private ProgramDisciplineId firstLinkId;
    private ProgramDisciplineId secondLinkId;
    private Program entity;
    private ProgramDiscipline firstLink;
    private ProgramDiscipline secondLink;
    private Course course;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        courseId = UUID.randomUUID();
        firstLinkId = new ProgramDisciplineId(UUID.randomUUID(), id);
        secondLinkId = new ProgramDisciplineId(UUID.randomUUID(), id);

        entity = new Program();
        course = new Course();

        course.setId(courseId);

        firstLink = new ProgramDiscipline();
        secondLink = new ProgramDiscipline();

        firstLink.setId(firstLinkId);
        secondLink.setId(secondLinkId);

        entity.setId(id);
        entity.setCourse(course);
        entity.setProgramDisciplines(List.of(firstLink, secondLink));
    }

    @Test
    void toEntity_shouldMapFieldsAndIgnoreIdAndProgramDisciplines_whenCalled() {
        final ProgramRequestDto request = new ProgramRequestDto(courseId);

        final Program result = programMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getCourse().getId()).isEqualTo(courseId),
            () -> assertThat(result.getId()).isNull(),
            () -> assertThat(result.getProgramDisciplines()).isEmpty()
        );
    }

    @Test
    void toResponseDto_shouldCopyAllFields_whenCalled() {
        final ProgramResponseDto result = programMapper.toResponseDto(entity);

        assertAll(
            () -> assertThat(result.id()).isEqualTo(entity.getId()),
            () -> assertThat(result.courseId()).isEqualTo(entity.getCourse().getId()),
            () -> assertThat(result.programDisciplinesIds()).containsExactly(firstLinkId,
                                                                             secondLinkId)
        );
    }

    @Test
    void toResponseDto_shouldContainNoProgramDisciplineIds_whenEntityHasNone() {
        final Program emptyEntity = new Program();

        final ProgramResponseDto result = programMapper.toResponseDto(emptyEntity);

        assertThat(result.programDisciplinesIds()).isEmpty();
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final ProgramResponseDto response = new ProgramResponseDto(
            id,
            courseId,
            List.of(firstLinkId, secondLinkId)
        );

        final List<ProgramResponseDto> result =
            programMapper.toResponseDtoList(List.of(entity));

        assertThat(result).containsExactly(response);
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<ProgramResponseDto> result =
            programMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateCourseLinkAndNothingElse_whenCalled() {
        final ProgramRequestDto requestDifferentCourse =
            new ProgramRequestDto(UUID.randomUUID());

        programMapper.updateEntityFromDto(requestDifferentCourse, entity);

        assertAll(
            () -> assertThat(entity.getCourse().getId())
                .isEqualTo(requestDifferentCourse.courseId()),
            () -> assertThat(entity.getProgramDisciplines())
                .containsExactly(firstLink, secondLink),
            () -> assertThat(entity.getCourse()).isNotSameAs(course),
            () -> assertThat(entity.getId()).isEqualTo(id),
            () -> assertThat(course.getId()).isEqualTo(courseId)
        );
    }
}
