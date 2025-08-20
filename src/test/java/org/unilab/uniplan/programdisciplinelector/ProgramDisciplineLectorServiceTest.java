package org.unilab.uniplan.programdisciplinelector;

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
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineLectorServiceTest {
    @Mock
    private ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @Mock
    private ProgramDisciplineLectorMapper programDisciplineLectorMapper;

    @InjectMocks
    private ProgramDisciplineLectorService programDisciplineLectorService;

    private ProgramDisciplineLectorId programDisciplineLectorId;
    private UUID lectorId;
    private UUID programId;
    private UUID disciplineId;
    private LectorType lectorType;
    private ProgramDisciplineLectorDto programDisciplineLectorDto;
    private ProgramDisciplineLector  programDisciplineLector;

    @BeforeEach
    void setUp() {
        lectorId = UUID.randomUUID();
        programId = UUID.randomUUID();
        disciplineId = UUID.randomUUID();
        programDisciplineLectorId = new ProgramDisciplineLectorId(lectorId, programId, disciplineId);

        lectorType = LectorType.BOTH;
        programDisciplineLectorDto = new ProgramDisciplineLectorDto(programDisciplineLectorId,
                                                                    lectorId,
                                                                    programId,
                                                                    disciplineId,
                                                                    lectorType);
         programDisciplineLector = new ProgramDisciplineLector();
    }

    @Test
    void testCreateProgramDisciplineShouldSaveAndReturnDto() {
        when(programDisciplineLectorMapper.toEntity(programDisciplineLectorDto)).thenReturn(programDisciplineLector);
        when(programDisciplineLectorRepository.save(programDisciplineLector)).thenReturn(programDisciplineLector);
        when(programDisciplineLectorMapper.toDto(programDisciplineLector)).thenReturn(programDisciplineLectorDto);

        ProgramDisciplineLectorDto result = programDisciplineLectorService.createProgramDisciplineLector(programDisciplineLectorDto);

        assertEquals(programDisciplineLectorDto, result);
    }

    @Test
    void testGetAllProgramDisciplinesShouldReturnListOfProgramDisciplineDtos() {
        List<ProgramDisciplineLector> entities = List.of(programDisciplineLector);
        List<ProgramDisciplineLectorDto> dtos = List.of(programDisciplineLectorDto);

        when(programDisciplineLectorRepository.findAll()).thenReturn(entities);
        when(programDisciplineLectorMapper.toDtos(entities)).thenReturn(dtos);

        List<ProgramDisciplineLectorDto> result = programDisciplineLectorService.getAllProgramDisciplineLectors();

        assertEquals(dtos, result);
    }

    @Test
    void testGetProgramDisciplineByIdShouldReturnProgramDisciplineDtoIfFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.of(programDisciplineLector));
        when(programDisciplineLectorMapper.toDto(programDisciplineLector)).thenReturn(programDisciplineLectorDto);
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);

        ProgramDisciplineLectorDto result = programDisciplineLectorService.getProgramDisciplineLectorById(
            lectorId,
            programId,
            disciplineId);

        assertEquals(programDisciplineLectorDto, result);
    }

    @Test
    void testGetProgramDisciplineByIdShouldReturnEmptyOptionalIfProgramDisciplineNotFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.empty());
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> programDisciplineLectorService.getProgramDisciplineLectorById(
                                                               lectorId,
                                                               programId,
                                                               disciplineId));

        assertTrue(exception.getMessage().contains(String.valueOf(programDisciplineLectorId)));
    }

    @Test
    void testUpdateProgramDisciplineShouldUpdateAndReturnDtoIfFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.of(programDisciplineLector));
        doAnswer(invocation -> null).when(programDisciplineLectorMapper).updateEntityFromDto(programDisciplineLectorDto, programDisciplineLector);
        when(programDisciplineLectorRepository.save(programDisciplineLector)).thenReturn(programDisciplineLector);
        when(programDisciplineLectorMapper.toDto(programDisciplineLector)).thenReturn(programDisciplineLectorDto);
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);

        ProgramDisciplineLectorDto result = programDisciplineLectorService.updateProgramDisciplineLector(
            lectorId,
            programId,
            disciplineId,
            programDisciplineLectorDto);

        assertEquals(programDisciplineLectorDto, result);
    }

    @Test
    void testUpdateProgramDisciplineShouldReturnEmptyOptionalIfNotFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.empty());
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> programDisciplineLectorService.updateProgramDisciplineLector(
                                                               lectorId,
                                                               programId,
                                                               disciplineId,
                                                               programDisciplineLectorDto));

        assertTrue(exception.getMessage().contains(String.valueOf(programDisciplineLectorId)));
    }

    @Test
    void testDeleteProgramDisciplineShouldDeleteProgramDisciplineIfFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.of(programDisciplineLector));
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);
        doAnswer(invocation -> null).when(programDisciplineLectorRepository).delete(programDisciplineLector);

        assertDoesNotThrow(() -> programDisciplineLectorService.deleteProgramDisciplineLector(lectorId,programId,disciplineId));
        verify(programDisciplineLectorRepository).delete(programDisciplineLector);
    }

    @Test
    void testDeleteProgramDisciplineShouldThrowIfNotFound() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(Optional.empty());
        when(programDisciplineLectorMapper.toProgramDisciplineLectorId(lectorId,
                                                                       programId,
                                                                       disciplineId)).thenReturn(
            programDisciplineLectorId);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            programDisciplineLectorService.deleteProgramDisciplineLector(lectorId,programId,disciplineId));

        assertTrue(exception.getMessage().contains(String.valueOf(lectorId)));
        assertTrue(exception.getMessage().contains(String.valueOf(programId)));
        assertTrue(exception.getMessage().contains(String.valueOf(disciplineId)));
    }
}