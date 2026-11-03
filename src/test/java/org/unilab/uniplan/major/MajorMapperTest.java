package org.unilab.uniplan.major;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseMapper;
import org.unilab.uniplan.course.dto.CourseResponseDto;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@ExtendWith(MockitoExtension.class)
class MajorMapperTest {

    @Mock
    private CourseMapper courseMapper;

    private MajorMapper mapper;

    private UUID facultyId;

    private final String majorName1 = "Business Information Technology";
    private final String majorName2 = "Software Engineering";
    private MajorRequestDto majorRequest;

    private Major major1;
    private Major major2;

    private CourseResponseDto courseResponseDto1;
    private CourseResponseDto courseResponseDto2;

    @BeforeEach
    void setUp() {

        mapper = new MajorMapperImpl(courseMapper);

        facultyId = UUID.randomUUID();
        majorRequest = new MajorRequestDto(facultyId, majorName1);

        final Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        final List<Course> coursesList = new ArrayList<>();

        major1 = new Major(faculty, majorName1, coursesList);
        major1.setId(UUID.randomUUID());

        major2 = new Major(faculty, majorName2, coursesList);
        major2.setId(UUID.randomUUID());

        final UUID courseResponseId1 = UUID.randomUUID();
        final UUID courseResponseId2 = UUID.randomUUID();

        courseResponseDto1 = new CourseResponseDto(courseResponseId1, major1.getId(),
                                                   (byte) 4,
                                                   "Regular",
                                                   "On site");
        courseResponseDto2 = new CourseResponseDto(courseResponseId2, major1.getId(),
                                                   (byte) 3,
                                                   "Regular",
                                                   "On site");
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        Major entity = mapper.toEntity(majorRequest);

        assertNull(entity.getId());
        assertNotNull(entity.getFaculty());
        assertAll(
            () -> assertEquals(entity.getFaculty().getId(), facultyId),
            () -> assertEquals(entity.getMajorName(), majorName1)
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenMajorIsValid() {
        MajorResponseDto majorResponseDto = mapper.toResponseDto(major1);

        assertAll(
            () -> assertEquals(majorResponseDto.facultyId(), facultyId),
            () -> assertEquals(majorResponseDto.id(), major1.getId()),
            () -> assertEquals(majorResponseDto.majorName(), major1.getMajorName())
        );
    }

    @Test
    void toFullResponseDto_shouldMapAllFields_whenMajorIsValid() {

        when(courseMapper.toResponseDtoList(major1.getCourses()))
            .thenReturn(List.of(courseResponseDto1, courseResponseDto2));

        MajorCoursesResponseDto majorCoursesResponseDto = mapper.toFullResponseDto(major1);

        assertNotNull(majorCoursesResponseDto.courses());
        assertAll(
            () -> assertEquals(majorCoursesResponseDto.facultyId(), facultyId),
            () -> assertEquals(majorCoursesResponseDto.id(), major1.getId()),
            () -> assertEquals(majorCoursesResponseDto.majorName(), major1.getMajorName())
        );
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        List<MajorResponseDto> majorResponseDtoList = mapper.toResponseDtoList(List.of(major1,
                                                                                       major2));

        assertThat(majorResponseDtoList)
            .extracting(MajorResponseDto::majorName)
            .containsExactly(majorName1, majorName2);
    }

    @Test
    void toFullResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        List<MajorCoursesResponseDto> majorCoursesResponseDtoList = mapper.toFullResponseDtoList(
            List.of(major1, major2));

        assertThat(majorCoursesResponseDtoList)
            .extracting(MajorCoursesResponseDto::majorName)
            .containsExactly(majorName1, majorName2);
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        List<MajorResponseDto> majorResponseDtoList = mapper.toResponseDtoList(List.of());

        assertThat(majorResponseDtoList).isEmpty();
    }

    @Test
    void toFullResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        List<MajorCoursesResponseDto> majorCoursesResponseDtoList = mapper.toFullResponseDtoList(
            List.of());

        assertThat(majorCoursesResponseDtoList).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        mapper.updateEntityFromDto(majorRequest, major2);

        assertNotNull(major2.getFaculty());
        assertAll(
            () -> assertEquals(majorRequest.facultyId(), major2.getFaculty().getId()),
            () -> assertEquals(majorRequest.majorName(), major2.getMajorName())
        );
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        UUID id = major2.getId();
        mapper.updateEntityFromDto(majorRequest, major2);

        assertEquals(id, major2.getId());
    }

    @Test
    void toFaculty_shouldMapId_whenFacultyIdIsGiven() {
        final Faculty result = mapper.toFaculty(facultyId);

        assertThat(result.getId()).isEqualTo(facultyId);
    }
}
