package org.unilab.uniplan.major;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseMapper;
import org.unilab.uniplan.course.CourseMapperImpl;
import org.unilab.uniplan.course.dto.CourseResponseDto;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

class MajorMapperTest {

    private final CourseMapper courseMapper = new CourseMapperImpl();

    private MajorMapper mapper;

    private UUID facultyIdEntity1;
    private UUID facultyIdDTO;
    private UUID facultyIdEntity2;

    private final String majorName1 = "Business Information Technology";
    private final String majorName2 = "Software Engineering";
    private MajorRequestDto majorRequest;

    private Major major1;
    private Major major2;

    @BeforeEach
    void setUp() {

        mapper = new MajorMapperImpl(courseMapper);

        facultyIdEntity1 = UUID.randomUUID();
        facultyIdDTO = UUID.randomUUID();
        facultyIdEntity2 = UUID.randomUUID();
        majorRequest = new MajorRequestDto(facultyIdDTO, majorName1);

        final Faculty faculty1 = new Faculty();
        faculty1.setId(facultyIdEntity1);

        final Faculty faculty2 = new Faculty();
        faculty2.setId(facultyIdEntity2);

        final Course course1 = new Course();
        course1.setCourseType("Regular");

        final Course course2 = new Course();
        course2.setCourseType("Part time");

        final List<Course> coursesListMajor1 = List.of(course1, course2);

        major1 = new Major(faculty1, majorName1, coursesListMajor1);
        major1.setId(UUID.randomUUID());

        major2 = new Major(faculty2, majorName2, coursesListMajor1);
        major2.setId(UUID.randomUUID());

    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Major entity = mapper.toEntity(majorRequest);

        assertNull(entity.getId());
        assertNotNull(entity.getFaculty());
        assertAll(
            () -> assertThat(entity.getFaculty().getId()).isEqualTo(facultyIdDTO),
            () -> assertThat(entity.getMajorName()).isEqualTo(majorName1)
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenMajorIsValid() {
        final MajorResponseDto majorResponseDto = mapper.toResponseDto(major1);

        assertAll(
            () -> assertThat(facultyIdEntity1).isEqualTo(majorResponseDto.facultyId()),
            () -> assertThat(majorResponseDto.id()).isEqualTo(major1.getId()),
            () -> assertThat(majorResponseDto.majorName()).isEqualTo(major1.getMajorName())
        );
    }

    @Test
    void toFullResponseDto_shouldMapAllFields_whenMajorIsValid() {

        final MajorCoursesResponseDto majorCoursesResponseDto = mapper.toFullResponseDto(major1);

        assertNotNull(majorCoursesResponseDto.courses());
        assertAll(
            () -> assertThat(majorCoursesResponseDto.facultyId()).isEqualTo(facultyIdEntity1),
            () -> assertThat(majorCoursesResponseDto.id()).isEqualTo(major1.getId()),
            () -> assertThat(majorCoursesResponseDto.majorName()).isEqualTo(major1.getMajorName())
        );
        assertThat(majorCoursesResponseDto.courses())
            .extracting(CourseResponseDto::courseType)
            .containsExactly("Regular", "Part time");
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        final List<MajorResponseDto> majorResponseDtoList = mapper.toResponseDtoList(List.of(major1,
                                                                                       major2));

        assertThat(majorResponseDtoList)
            .extracting(MajorResponseDto::majorName)
            .containsExactly(majorName1, majorName2);
    }

    @Test
    void toFullResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        final List<MajorCoursesResponseDto> majorCoursesResponseDtoList = mapper.toFullResponseDtoList(
            List.of(major1, major2));

        assertThat(majorCoursesResponseDtoList)
            .extracting(MajorCoursesResponseDto::majorName)
            .containsExactly(majorName1, majorName2);

        assertThat(majorCoursesResponseDtoList)
            .extracting(MajorCoursesResponseDto::courses)
            .allSatisfy(courses ->
                            assertThat(courses)
                                .extracting(CourseResponseDto::courseType)
                                .containsExactly("Regular", "Part time")
            );
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final List<MajorResponseDto> majorResponseDtoList = mapper.toResponseDtoList(List.of());

        assertThat(majorResponseDtoList).isEmpty();
    }

    @Test
    void toFullResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final List<MajorCoursesResponseDto> majorCoursesResponseDtoList = mapper.toFullResponseDtoList(
            List.of());

        assertThat(majorCoursesResponseDtoList).isEmpty();
    }

    @Test
    void updateEntityFromDto_shouldUpdateAllFields_whenRequestDtoIsValid() {
        final Faculty original = major2.getFaculty();
        mapper.updateEntityFromDto(majorRequest, major2);

        assertThat(original.getId()).isEqualTo(facultyIdEntity2);

        assertNotNull(major2.getFaculty());
        assertAll(
            () -> assertThat(majorRequest.facultyId()).isEqualTo(major2.getFaculty().getId()),
            () -> assertThat(majorRequest.majorName()).isEqualTo(major2.getMajorName())
        );
        assertThat(major2.getFaculty()).isNotSameAs(original);
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final UUID id = major2.getId();
        mapper.updateEntityFromDto(majorRequest, major2);

        assertThat(id).isEqualTo(major2.getId());
    }

    @Test
    void toFaculty_shouldMapId_whenFacultyIdIsGiven() {
        final Faculty result = mapper.toFaculty(facultyIdEntity1);

        assertThat(result.getId()).isEqualTo(facultyIdEntity1);
    }
}
