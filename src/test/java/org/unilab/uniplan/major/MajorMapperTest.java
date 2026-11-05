package org.unilab.uniplan.major;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private UUID facultyIdEntity;
    private UUID facultyIdDTO;

    private final String majorName1 = "Business Information Technology";
    private final String majorName2 = "Software Engineering";
    private MajorRequestDto majorRequest;

    private Major major1;
    private Major major2;

    @BeforeEach
    void setUp() {

        mapper = new MajorMapperImpl(courseMapper);

        facultyIdEntity = UUID.randomUUID();
        facultyIdDTO = UUID.randomUUID();
        majorRequest = new MajorRequestDto(facultyIdDTO, majorName1);

        final Faculty faculty = new Faculty();
        faculty.setId(facultyIdEntity);

        final Course course1 = new Course();
        course1.setCourseType("Regular");

        final Course course2 = new Course();
        course2.setCourseType("Part time");

        final List<Course> coursesListMajor1 = List.of(course1, course2);

        major1 = new Major(faculty, majorName1, coursesListMajor1);
        major1.setId(UUID.randomUUID());

        major2 = new Major(faculty, majorName2, coursesListMajor1);
        major2.setId(UUID.randomUUID());

    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Major entity = mapper.toEntity(majorRequest);

        assertNull(entity.getId());
        assertNotNull(entity.getFaculty());
        assertAll(
            () -> assertEquals(entity.getFaculty().getId(), facultyIdDTO),
            () -> assertEquals(entity.getMajorName(), majorName1)
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenMajorIsValid() {
        final MajorResponseDto majorResponseDto = mapper.toResponseDto(major1);

        assertAll(
            () -> assertEquals(majorResponseDto.facultyId(), facultyIdEntity),
            () -> assertEquals(majorResponseDto.id(), major1.getId()),
            () -> assertEquals(majorResponseDto.majorName(), major1.getMajorName())
        );
    }

    @Test
    void toFullResponseDto_shouldMapAllFields_whenMajorIsValid() {

        final MajorCoursesResponseDto majorCoursesResponseDto = mapper.toFullResponseDto(major1);

        assertNotNull(majorCoursesResponseDto.courses());
        assertAll(
            () -> assertEquals(majorCoursesResponseDto.facultyId(), facultyIdEntity),
            () -> assertEquals(majorCoursesResponseDto.id(), major1.getId()),
            () -> assertEquals(majorCoursesResponseDto.majorName(), major1.getMajorName())
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
        mapper.updateEntityFromDto(majorRequest, major2);

        assertNotNull(major2.getFaculty());
        assertAll(
            () -> assertEquals(majorRequest.facultyId(), major2.getFaculty().getId()),
            () -> assertEquals(majorRequest.majorName(), major2.getMajorName())
        );
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final UUID id = major2.getId();
        mapper.updateEntityFromDto(majorRequest, major2);

        assertEquals(id, major2.getId());
    }

    @Test
    void toFaculty_shouldMapId_whenFacultyIdIsGiven() {
        final Faculty result = mapper.toFaculty(facultyIdEntity);

        assertThat(result.getId()).isEqualTo(facultyIdEntity);
    }
}
