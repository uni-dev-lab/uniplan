package org.unilab.uniplan.discipline;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.discipline.dto.DisciplineDto;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {

    @Mock
    private DisciplineMapper disciplineMapper;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    private UUID id;
    private String name;
    private String mainLector;
    private List<ProgramDisciplineId> programDisciplineIds = new ArrayList<>();
    private DisciplineDto disciplineDto;
    private Discipline discipline;

    @BeforeEach
    void setUp(){
        int programDisciplineCount = 3;
        id = UUID.randomUUID();
        name = "Programing";
        mainLector = "Ivan Ivanov";
        for (int i = 0; i < programDisciplineCount; i++) {
            programDisciplineIds.add(new ProgramDisciplineId(UUID.randomUUID(), UUID.randomUUID()));
        }
        disciplineDto = new DisciplineDto(id, name, mainLector, programDisciplineIds);
        discipline = new Discipline();
    }

    @Test
    void testCreateDisciplineShouldSaveAndReturnDto() {
        when(disciplineMapper.toEntity(disciplineDto)).thenReturn(discipline);
        when(disciplineRepository.save(discipline)).thenReturn(discipline);
        when(disciplineMapper.toDto(discipline)).thenReturn(disciplineDto);

        DisciplineDto result = disciplineService.createDiscipline(disciplineDto);

        assertEquals(disciplineDto, result);
    }

    @Test
    void testGetAllDisciplinesShouldReturnListOfDisciplieDtos() {
        List<Discipline> entities = List.of(discipline);
        List<DisciplineDto> dtos = List.of(disciplineDto);

        when(disciplineRepository.findAll()).thenReturn(entities);
        when(disciplineMapper.toDtoList(entities)).thenReturn(dtos);

        List<DisciplineDto> result = disciplineService.getAllDisciplines();

        assertEquals(dtos, result);
    }

    @Test
    void testGetDisciplieByIdShouldReturnDisciplineDtoIfFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));
        when(disciplineMapper.toDto(discipline)).thenReturn(disciplineDto);

        Optional<DisciplineDto> result = disciplineService.getDisciplineById(id);

        assertTrue(result.isPresent());
        assertEquals(disciplineDto, result.get());
    }

    @Test
    void testGetDisciplineByIdShouldReturnEmptyOptionalIfDisciplineNotFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DisciplineDto> result = disciplineService.getDisciplineById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateDisciplineShouldUpdateAndReturnDtoIfFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));
        doAnswer(invocation -> null).when(disciplineMapper).updateEntityFromDto(disciplineDto, discipline);
        when(disciplineRepository.save(discipline)).thenReturn(discipline);
        when(disciplineMapper.toDto(discipline)).thenReturn(disciplineDto);

        Optional<DisciplineDto> result = disciplineService.updateDiscipline(id, disciplineDto);

        assertTrue(result.isPresent());
        assertEquals(disciplineDto, result.get());
    }

    @Test
    void testUpdateDisciplineShouldReturnEmptyOptionalIfNotFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DisciplineDto> result = disciplineService.updateDiscipline(id, disciplineDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteDisciplineShouldDeleteDisciplineIfFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));
        doAnswer(invocation -> null).when(disciplineRepository).delete(discipline);

        assertDoesNotThrow(() -> disciplineService.deleteDiscipline(id));
        verify(disciplineRepository).delete(discipline);
    }

    @Test
    void testDeleteDisciplineShouldThrowIfNotFound() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            disciplineService.deleteDiscipline(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}