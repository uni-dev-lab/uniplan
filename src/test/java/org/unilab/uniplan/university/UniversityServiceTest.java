package org.unilab.uniplan.university;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.dto.UniversityDto;

@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {

    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private UniversityMapper universityMapper;

    @InjectMocks
    private UniversityService universityService;

    private UUID id;
    private University entity;
    private UniversityDto dto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new University();
        dto = new UniversityDto(id,
                                "University of Sofia",
                                "Sofia",
                                (short) 1888,
                                "Excellent",
                                "www.uni-sofia.bg");
    }

    @Test
    void testCreateUniversityShouldSaveAndReturnDto() {
        when(universityMapper.toEntity(dto)).thenReturn(entity);
        when(universityRepository.save(entity)).thenReturn(entity);
        when(universityMapper.toDto(entity)).thenReturn(dto);

        UniversityDto result = universityService.createUniversity(dto);

        assertEquals(dto, result);
    }

    @Test
    void testGetAllUniversitiesShouldReturnListOfUniversityDtos() {
        List<University> entities = List.of(entity);
        List<UniversityDto> dtos = List.of(dto);

        when(universityRepository.findAll()).thenReturn(entities);
        when(universityMapper.toDtoList(entities)).thenReturn(dtos);

        List<UniversityDto> result = universityService.getAllUniversities();

        assertEquals(dtos, result);
    }

    @Test
    void testGetUniversityByIdShouldReturnUniversityDtoIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(universityMapper.toDto(entity)).thenReturn(dto);

        UniversityDto result = universityService.getUniversityById(id);

        assertEquals(dto, result);
    }

    @Test
    void testGetUniversityByIdShouldReturnEmptyOptionalIfUniversityNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> universityService.getUniversityById(
                                                               id));
        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testUpdateUniversityShouldUpdateAndReturnDtoIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(universityMapper).updateEntityFromDto(dto, entity);
        when(universityRepository.save(entity)).thenReturn(entity);
        when(universityMapper.toDto(entity)).thenReturn(dto);

        UniversityDto result = universityService.updateUniversity(id, dto);

        assertEquals(dto, result);
    }

    @Test
    void testUpdateUniversityShouldReturnEmptyOptionalIfNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> universityService.updateUniversity(
                                                               id,
                                                               dto));
        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testDeleteUniversityShouldDeleteUniversityIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(universityRepository).delete(entity);

        assertDoesNotThrow(() -> universityService.deleteUniversity(id));
        verify(universityRepository).delete(entity);
    }

    @Test
    void testDeleteUniversityShouldThrowIfNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                  () -> universityService.deleteUniversity(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }
}
