package org.unilab.uniplan.program;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

class ProgramMapperTest {

    private final ProgramMapper programMapper = new ProgramMapperImpl();

    @Test
    void toEntity_shouldMapFieldsAndIgnoreIdAndProgramDisciplines_whenCalled() {
        final UUID courseId = UUID.randomUUID();
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
        final UUID id = UUID.randomUUID();
        final UUID courseId = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Program program =
            createProgram(id, createCourse(courseId), firstLinkId, secondLinkId);

        final ProgramResponseDto result = programMapper.toResponseDto(program);

        assertAll(
            () -> assertThat(result.id()).isEqualTo(id),
            () -> assertThat(result.courseId()).isEqualTo(courseId),
            () -> assertThat(result.programDisciplinesIds())
                .containsExactly(firstLinkId, secondLinkId)
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
        final UUID id = UUID.randomUUID();
        final UUID courseId = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Program program =
            createProgram(id, createCourse(courseId), firstLinkId, secondLinkId);
        final ProgramResponseDto response = new ProgramResponseDto(
            id,
            courseId,
            List.of(firstLinkId, secondLinkId)
        );

        final List<ProgramResponseDto> result =
            programMapper.toResponseDtoList(List.of(program));

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
        final UUID id = UUID.randomUUID();
        final UUID courseId = UUID.randomUUID();
        final ProgramDisciplineId firstLinkId = createProgramDisciplineId(id);
        final ProgramDisciplineId secondLinkId = createProgramDisciplineId(id);
        final Course originalCourse = createCourse(courseId);
        final Program program = createProgram(id, originalCourse, firstLinkId, secondLinkId);
        final ProgramRequestDto request = new ProgramRequestDto(UUID.randomUUID());

        programMapper.updateEntityFromDto(request, program);

        assertAll(
            () -> assertThat(program.getCourse().getId()).isEqualTo(request.courseId()),
            () -> assertThat(program.getCourse()).isNotSameAs(originalCourse),
            () -> assertThat(originalCourse.getId()).isEqualTo(courseId),
            () -> assertThat(program.getId()).isEqualTo(id),
            () -> assertThat(program.getProgramDisciplines()
                                    .stream()
                                    .map(ProgramDiscipline::getId))
                .containsExactly(firstLinkId, secondLinkId)
        );
    }

    private Program createProgram(final UUID id,
                                  final Course course,
                                  final ProgramDisciplineId... programDisciplineIds) {
        final Program program = new Program();
        program.setId(id);
        program.setCourse(course);
        program.setProgramDisciplines(Arrays.stream(programDisciplineIds)
                                            .map(this::createProgramDiscipline)
                                            .toList());
        return program;
    }

    private ProgramDisciplineId createProgramDisciplineId(final UUID programId) {
        return new ProgramDisciplineId(UUID.randomUUID(), programId);
    }

    private Course createCourse(final UUID id) {
        final Course course = new Course();
        course.setId(id);
        return course;
    }

    private ProgramDiscipline createProgramDiscipline(final ProgramDisciplineId id) {
        final ProgramDiscipline programDiscipline = new ProgramDiscipline();
        programDiscipline.setId(id);
        return programDiscipline;
    }
}