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

@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {@Mock
private UniversityRepository universityRepository;

    @InjectMocks
    private UniversityService universityService;

    private UUID id;
    private University entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new University();
    }

    @Test
    void testCreateUniversityShouldSaveAndReturnDto() {
        when(universityRepository.save(entity)).thenReturn(entity);

        University result = universityService.createUniversity(entity);

        assertEquals(entity, result);
    }

    @Test
    void testGetAllUniversitiesShouldReturnListOfUniversityDtos() {
        List<University> entities = List.of(entity);

        when(universityRepository.findAll()).thenReturn(entities);

        List<University> result = universityService.getAllUniversities();

        assertEquals(entities, result);
    }

    @Test
    void testGetUniversityByIdShouldReturnUniversityDtoIfFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<University> result = universityService.getUniversityById(id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void testGetUniversityByIdShouldReturnEmptyOptionalIfUniversityNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<University> result = universityService.getUniversityById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUniversityShouldUpdateAndReturnDtoIfFound() {
        when(universityRepository.save(entity)).thenReturn(entity);

        University result = universityService.updateUniversity(entity);

        assertEquals(entity, result);
        verify(universityRepository).save(entity);
    }

    @Test
    void testDeleteUniversityShouldDeleteUniversityIfFound() {
        doAnswer(invocation -> null).when(universityRepository).delete(entity);

        assertDoesNotThrow(() -> universityService.deleteUniversity(entity));
        verify(universityRepository).delete(entity);
    }
}
