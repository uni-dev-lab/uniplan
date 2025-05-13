package org.unilab.uniplan.university;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
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
        List<University> entities = List.of(new University());
        List<UniversityDto> dtos = List.of(new UniversityDto(UUID.randomUUID(),
                                                             "Technical University",
                                                             "Plovdiv",
                                                             (short) 1962,
                                                             "Good",
                                                             "www.tu-plovdiv.bg"));

        when(universityRepository.findAll()).thenReturn(entities);
        when(universityMapper.toDtoList(entities)).thenReturn(dtos);

        List<UniversityDto> result = universityService.getAllUniversities();

        assertEquals(dtos, result);
    }

    @Test
    void testGetUniversityByIdShouldReturnUniversityDtoIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(universityMapper.toDto(entity)).thenReturn(dto);

        Optional<UniversityDto> result = universityService.getUniversityById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetUniversityByIdShouldReturnEmptyOptionalIfUniversityNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UniversityDto> result = universityService.getUniversityById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUniversityShouldUpdateAndReturnDtoIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(universityMapper).updateEntityFromDto(dto, entity);
        when(universityRepository.save(entity)).thenReturn(entity);
        when(universityMapper.toDto(entity)).thenReturn(dto);

        Optional<UniversityDto> result = universityService.updateUniversity(id, dto);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testUpdateUniversityShouldReturnEmptyOptionalIfNotFound() {
        UniversityDto updatedDto = new UniversityDto(id,
                                                     "Plovdiv University",
                                                     "Plovdiv",
                                                     (short) 1961,
                                                     "Good",
                                                     "www.uni-plovdiv.bg");

        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UniversityDto> result = universityService.updateUniversity(id, updatedDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUniversityShouldDeleteUniversityIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(universityRepository).delete(entity);

        assertDoesNotThrow(() -> universityService.deleteUniversity(id));
        verify(universityRepository).delete(entity);
    }

    @Test
    void testDeleteUniversityShouldThrowIfNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> universityService.deleteUniversity(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}
