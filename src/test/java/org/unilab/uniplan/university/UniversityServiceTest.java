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

    @Test
    void testCreateUniversityShouldSaveAndReturnDto() {
        UUID id = UUID.randomUUID();
        String uniName = "University of Sofia";
        String location = "Sofia";
        short establishedYear = 1888;
        String accreditation = "Excellent";
        String website = "www.uni-sofia.bg";

        UniversityDto dto = new UniversityDto(id,
                                              uniName,
                                              location,
                                              establishedYear,
                                              accreditation,
                                              website);
        University entity = new University();
        University saved = new University();
        UniversityDto savedDto = new UniversityDto(id,
                                                   uniName,
                                                   location,
                                                   establishedYear,
                                                   accreditation,
                                                   website);

        when(universityMapper.toEntity(dto)).thenReturn(entity);
        when(universityRepository.save(entity)).thenReturn(saved);
        when(universityMapper.toDto(saved)).thenReturn(savedDto);

        UniversityDto result = universityService.createUniversity(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testGetAllUniversitiesShouldReturnListOfUniversityDtos() {
        List<University> entities = List.of(new University());
        List<UniversityDto> dtos = List.of(
            new UniversityDto(UUID.randomUUID(), "Technical University",
                              "Plovdiv", (short) 1962, "Good", "www.tu-plovdiv.bg"));

        when(universityRepository.findAll()).thenReturn(entities);
        when(universityMapper.toDtoList(entities)).thenReturn(dtos);

        List<UniversityDto> result = universityService.getAllUniversities();

        assertEquals(dtos, result);
    }

    @Test
    void testGetUniversityByIdShouldReturnUniversityDtoIfFound() {
        UUID id = UUID.randomUUID();
        University entity = new University();
        UniversityDto dto = new UniversityDto(id, "New Bulgarian University",
                                              "Sofia", (short) 1991, "Very Good", "www.nbu.bg");

        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(universityMapper.toDto(entity)).thenReturn(dto);

        Optional<UniversityDto> result = universityService.getUniversityById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetUniversityByIdShouldReturnEmptyOptionalIfUniversityNotFound() {
        UUID id = UUID.randomUUID();

        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UniversityDto> result = universityService.getUniversityById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUniversityShouldUpdateAndReturnDtoIfFound() {
        UUID id = UUID.randomUUID();
        String updatedName = "American University in Bulgaria";
        String updatedLocation = "Blagoevgrad";
        short updatedEstablishedYear = 1991;
        String updatedAccreditation = "Excellent";
        String updatedWebsite = "www.aubg.edu";

        UniversityDto dto = new UniversityDto(id,
                                              updatedName,
                                              updatedLocation,
                                              updatedEstablishedYear,
                                              updatedAccreditation,
                                              updatedWebsite);
        University existing = new University();
        University updated = new University();
        UniversityDto updatedDto = new UniversityDto(id,
                                                     updatedName,
                                                     updatedLocation,
                                                     updatedEstablishedYear,
                                                     updatedAccreditation,
                                                     updatedWebsite);

        when(universityRepository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> null).when(universityMapper).updateEntityFromDto(dto, existing);
        when(universityRepository.save(existing)).thenReturn(updated);
        when(universityMapper.toDto(updated)).thenReturn(updatedDto);

        Optional<UniversityDto> result = universityService.updateUniversity(id, dto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }

    @Test
    void testUpdateUniversityShouldReturnEmptyOptionalIfNotFound() {
        UUID id = UUID.randomUUID();
        UniversityDto dto = new UniversityDto(id,
                                              "Plovdiv University",
                                              "Plovdiv",
                                              (short) 1961,
                                              "Good",
                                              "www.uni-plovdiv.bg");

        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UniversityDto> result = universityService.updateUniversity(id, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUniversityShouldDeleteUniversityIfFound() {
        UUID id = UUID.randomUUID();
        University entity = new University();

        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(universityRepository).delete(entity);

        assertDoesNotThrow(() -> universityService.deleteUniversity(id));
        verify(universityRepository).delete(entity);
    }

    @Test
    void testDeleteUniversityShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            universityService.deleteUniversity(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}