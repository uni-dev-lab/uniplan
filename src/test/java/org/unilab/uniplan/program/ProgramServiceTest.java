package org.unilab.uniplan.program;


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
import org.unilab.uniplan.program.dto.ProgramDto;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapper programMapper;

    @InjectMocks
    private ProgramService programService;

    private UUID id;
    private UUID courseId;
    private List<ProgramDisciplineId> programDisciplineIds = new ArrayList<>();
    private ProgramDto programDto;
    private Program program;

    @BeforeEach
    void setUp(){
        int programDisciplineCount = 3;
        id = UUID.randomUUID();
        courseId = UUID.randomUUID();
        for (int i = 0; i < programDisciplineCount; i++) {
            programDisciplineIds.add(new ProgramDisciplineId(UUID.randomUUID(),UUID.randomUUID()));
        }
        programDto = new ProgramDto(id,courseId,programDisciplineIds);
    }

    @Test
    void testCreateProgramShouldSaveAndReturnDto() {
        when(programMapper.toEntity(programDto)).thenReturn(program);
        when(programRepository.save(program)).thenReturn(program);
        when(programMapper.toDto(program)).thenReturn(programDto);

        ProgramDto result = programService.createProgram(programDto);

        assertEquals(programDto, result);
    }

    @Test
    void testGetAllProgramsShouldReturnListOfProgramDtos() {
        List<Program> entities = List.of(program);
        List<ProgramDto> dtos = List.of(programDto);

        when(programRepository.findAll()).thenReturn(entities);
        when(programMapper.toDtoList(entities)).thenReturn(dtos);

        List<ProgramDto> result = programService.getAllPrograms();

        assertEquals(dtos, result);
    }

    @Test
    void testGetProgramByIdShouldReturnProgramDtoIfFound() {
        when(programRepository.findById(id)).thenReturn(Optional.of(program));
        when(programMapper.toDto(program)).thenReturn(programDto);

        Optional<ProgramDto> result = programService.getProgramById(id);

        assertTrue(result.isPresent());
        assertEquals(programDto, result.get());
    }

    @Test
    void testGetProgramByIdShouldReturnEmptyOptionalIfProgramNotFound() {
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProgramDto> result = programService.getProgramById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateProgramShouldUpdateAndReturnDtoIfFound() {
        when(programRepository.findById(id)).thenReturn(Optional.of(program));
        doAnswer(invocation -> null).when(programMapper).updateEntityFromDto(programDto, program);
        when(programRepository.save(program)).thenReturn(program);
        when(programMapper.toDto(program)).thenReturn(programDto);

        Optional<ProgramDto> result = programService.updateProgram(id, programDto);

        assertTrue(result.isPresent());
        assertEquals(programDto, result.get());
    }

    @Test
    void testUpdateProgramShouldReturnEmptyOptionalIfNotFound() {
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProgramDto> result = programService.updateProgram(id, programDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteProgramShouldDeleteProgramIfFound() {
        when(programRepository.findById(id)).thenReturn(Optional.of(program));
        doAnswer(invocation -> null).when(programRepository).delete(program);

        assertDoesNotThrow(() -> programService.deleteProgram(id));
        verify(programRepository).delete(program);
    }

    @Test
    void testDeleteProgramShouldThrowIfNotFound() {
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            programService.deleteProgram(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}