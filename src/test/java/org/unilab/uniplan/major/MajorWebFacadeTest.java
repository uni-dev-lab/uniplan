package org.unilab.uniplan.major;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@ExtendWith(MockitoExtension.class)
class MajorWebFacadeTest {

    @Mock
    private MajorService majorService;

    @Mock
    private MajorMapper majorMapper;

    @Mock
    private MajorValidator majorValidator;

    @InjectMocks
    private MajorWebFacade majorWebFacade;

    private UUID majorId;
    private UUID facultyId;
    private Major major;
    private MajorRequestDto requestDto;
    private MajorResponseDto responseDto;
    private MajorCoursesResponseDto coursesResponseDto;

    @BeforeEach
    void setUp() {
        majorId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        major = new Major();
        requestDto = mock(MajorRequestDto.class);
        responseDto = mock(MajorResponseDto.class);
        coursesResponseDto = mock(MajorCoursesResponseDto.class);
    }

    @Test
    void createMajorShouldValidateMapAndSaveMajor() {
        when(majorMapper.toEntity(requestDto)).thenReturn(major);

        majorWebFacade.createMajor(requestDto);

        final InOrder inOrder = inOrder(majorValidator, majorMapper, majorService);
        inOrder.verify(majorValidator).validateForCreate(requestDto);
        inOrder.verify(majorMapper).toEntity(requestDto);
        inOrder.verify(majorService).save(major);
    }

    @Test
    void getAllMajorsShouldReturnResponseDtoList() {
        final List<Major> majors = List.of(major);
        final List<MajorResponseDto> responseDtos = List.of(responseDto);

        when(majorService.getAll()).thenReturn(majors);
        when(majorMapper.toResponseDtoList(majors)).thenReturn(responseDtos);

        final List<MajorResponseDto> result = majorWebFacade.getAllMajors();

        assertEquals(responseDtos, result);
        verify(majorService).getAll();
        verify(majorMapper).toResponseDtoList(majors);
    }

    @Test
    void getMajorByIdShouldReturnResponseDtoIfFound() {
        when(majorService.getById(majorId)).thenReturn(Optional.of(major));
        when(majorMapper.toResponseDto(major)).thenReturn(responseDto);

        final MajorResponseDto result = majorWebFacade.getMajorById(majorId);

        assertEquals(responseDto, result);
        verify(majorService).getById(majorId);
        verify(majorMapper).toResponseDto(major);
    }

    @Test
    void getMajorByIdShouldThrowIfMajorDoesNotExist() {
        when(majorService.getById(majorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> majorWebFacade.getMajorById(majorId));

        verify(majorService).getById(majorId);
        verify(majorMapper, never()).toResponseDto(any(Major.class));
    }

    @Test
    void getMajorWithCoursesByIdShouldReturnCoursesResponseDtoIfFound() {
        when(majorService.getById(majorId)).thenReturn(Optional.of(major));
        when(majorMapper.toFullResponseDto(major)).thenReturn(coursesResponseDto);

        final MajorCoursesResponseDto result = majorWebFacade.getMajorWithCoursesById(majorId);

        assertEquals(coursesResponseDto, result);
        verify(majorService).getById(majorId);
        verify(majorMapper).toFullResponseDto(major);
    }

    @Test
    void getMajorWithCoursesByIdShouldThrowIfMajorDoesNotExist() {
        when(majorService.getById(majorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> majorWebFacade.getMajorWithCoursesById(majorId));

        verify(majorService).getById(majorId);
        verify(majorMapper, never()).toFullResponseDto(any(Major.class));
    }

    @Test
    void getMajorsByFacultyIdShouldReturnResponseDtoList() {
        final List<Major> majors = List.of(major);
        final List<MajorResponseDto> responseDtos = List.of(responseDto);

        when(majorService.findAllMajorByFacultyId(facultyId)).thenReturn(majors);
        when(majorMapper.toResponseDtoList(majors)).thenReturn(responseDtos);

        final List<MajorResponseDto> result = majorWebFacade.getMajorsByFacultyId(facultyId);

        assertEquals(responseDtos, result);
        verify(majorService).findAllMajorByFacultyId(facultyId);
        verify(majorMapper).toResponseDtoList(majors);
    }

    @Test
    void getMajorsWithCoursesByFacultyIdShouldReturnCoursesResponseDtoList() {
        final List<Major> majors = List.of(major);
        final List<MajorCoursesResponseDto> responseDtos = List.of(coursesResponseDto);

        when(majorService.findAllMajorWithCoursesByFacultyId(facultyId)).thenReturn(majors);
        when(majorMapper.toFullResponseDtoList(majors)).thenReturn(responseDtos);

        final List<MajorCoursesResponseDto> result =
            majorWebFacade.getMajorsWithCoursesByFacultyId(facultyId);

        assertEquals(responseDtos, result);
        verify(majorService).findAllMajorWithCoursesByFacultyId(facultyId);
        verify(majorMapper).toFullResponseDtoList(majors);
    }

    @Test
    void updateMajorShouldValidateUpdateMapAndSaveMajorIfFound() {
        when(majorService.getById(majorId)).thenReturn(Optional.of(major));

        majorWebFacade.updateMajor(majorId, requestDto);

        final InOrder inOrder = inOrder(majorValidator, majorService, majorMapper);
        inOrder.verify(majorValidator).validateForUpdate(requestDto);
        inOrder.verify(majorService).getById(majorId);
        inOrder.verify(majorMapper).updateEntityFromDto(requestDto, major);
        inOrder.verify(majorService).save(major);
    }

    @Test
    void updateMajorShouldThrowIfMajorDoesNotExist() {
        when(majorService.getById(majorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> majorWebFacade.updateMajor(majorId, requestDto));

        verify(majorValidator).validateForUpdate(requestDto);
        verify(majorService).getById(majorId);
        verify(majorMapper, never()).updateEntityFromDto(any(MajorRequestDto.class), any(Major.class));
        verify(majorService, never()).save(any(Major.class));
    }

    @Test
    void deleteMajorShouldDeleteMajorIfFound() {
        when(majorService.getById(majorId)).thenReturn(Optional.of(major));

        majorWebFacade.deleteMajor(majorId);

        verify(majorService).getById(majorId);
        verify(majorService).delete(major);
    }

    @Test
    void deleteMajorShouldThrowIfMajorDoesNotExist() {
        when(majorService.getById(majorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> majorWebFacade.deleteMajor(majorId));

        verify(majorService).getById(majorId);
        verify(majorService, never()).delete(any(Major.class));
    }
}