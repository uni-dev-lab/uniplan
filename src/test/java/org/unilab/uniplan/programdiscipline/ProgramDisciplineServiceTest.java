package org.unilab.uniplan.programdiscipline;

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
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineServiceTest {

    @Mock
    private ProgramDisciplineRepository programDisciplineRepository;

    @Mock
    private ProgramDisciplineMapper programDisciplineMapper;

    @InjectMocks
    private ProgramDisciplineService programDisciplineService;

    private ProgramDisciplineId id;
    private UUID disciplineId;
    private UUID programId;
    private short hoursLecture;
    private short hoursExercise;
    private byte semesterCount;
    private ProgramDisciplineDto programDisciplineDto;
    private ProgramDiscipline programDiscipline;

    @BeforeEach
    void setUp(){
        id = new ProgramDisciplineId(UUID.randomUUID(),UUID.randomUUID());
        disciplineId = UUID.randomUUID();
        programId = UUID.randomUUID();
        hoursExercise = 12;
        hoursLecture = 12;
        semesterCount = 4;
        programDisciplineDto = new ProgramDisciplineDto(id,disciplineId,programId,hoursLecture,hoursExercise,semesterCount);
        programDiscipline = new ProgramDiscipline();
    }

    @Test
    void testCreateProgramDisciplineShouldSaveAndReturnDto() {
        when(programDisciplineMapper.toEntity(programDisciplineDto)).thenReturn(programDiscipline);
        when(programDisciplineRepository.save(programDiscipline)).thenReturn(programDiscipline);
        when(programDisciplineMapper.toDto(programDiscipline)).thenReturn(programDisciplineDto);

        ProgramDisciplineDto result = programDisciplineService.createProgramDiscipline(programDisciplineDto);

        assertEquals(programDisciplineDto, result);
    }

    @Test
    void testGetAllProgramDisciplinesShouldReturnListOfProgramDisciplineDtos() {
        List<ProgramDiscipline> entities = List.of(programDiscipline);
        List<ProgramDisciplineDto> dtos = List.of(programDisciplineDto);

        when(programDisciplineRepository.findAll()).thenReturn(entities);
        when(programDisciplineMapper.toDtos(entities)).thenReturn(dtos);

        List<ProgramDisciplineDto> result = programDisciplineService.getAllProgramDisciplines();

        assertEquals(dtos, result);
    }

    @Test
    void testGetProgramDisciplineByIdShouldReturnProgramDisciplineDtoIfFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.of(programDiscipline));
        when(programDisciplineMapper.toDto(programDiscipline)).thenReturn(programDisciplineDto);

        Optional<ProgramDisciplineDto> result = programDisciplineService.getProgramDisciplineById(id);

        assertTrue(result.isPresent());
        assertEquals(programDisciplineDto, result.get());
    }

    @Test
    void testGetProgramDisciplineByIdShouldReturnEmptyOptionalIfProgramDisciplineNotFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProgramDisciplineDto> result = programDisciplineService.getProgramDisciplineById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateProgramDisciplineShouldUpdateAndReturnDtoIfFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.of(programDiscipline));
        doAnswer(invocation -> null).when(programDisciplineMapper).updateEntityFromDto(programDisciplineDto, programDiscipline);
        when(programDisciplineRepository.save(programDiscipline)).thenReturn(programDiscipline);
        when(programDisciplineMapper.toDto(programDiscipline)).thenReturn(programDisciplineDto);

        Optional<ProgramDisciplineDto> result = programDisciplineService.updateProgramDiscipline(id, programDisciplineDto);

        assertTrue(result.isPresent());
        assertEquals(programDisciplineDto, result.get());
    }

    @Test
    void testUpdateProgramDisciplineShouldReturnEmptyOptionalIfNotFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProgramDisciplineDto> result = programDisciplineService.updateProgramDiscipline(id, programDisciplineDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteProgramDisciplineShouldDeleteProgramDisciplineIfFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.of(programDiscipline));
        doAnswer(invocation -> null).when(programDisciplineRepository).delete(programDiscipline);

        assertDoesNotThrow(() -> programDisciplineService.deleteProgramDiscipline(id));
        verify(programDisciplineRepository).delete(programDiscipline);
    }

    @Test
    void testDeleteProgramDisciplineShouldThrowIfNotFound() {
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            programDisciplineService.deleteProgramDiscipline(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}