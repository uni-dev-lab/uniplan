package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unilab.uniplan.utils.ErrorConstants.LECTOR_NOT_FOUND;

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
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineLectorWebFacadeTest {

    @Mock
    private ProgramDisciplineLectorService programDisciplineLectorService;

    @Mock
    private ProgramDisciplineLectorMapper programDisciplineLectorMapper;

    @Mock
    private ProgramDisciplineLectorValidator programDisciplineLectorValidator;

    @InjectMocks
    private ProgramDisciplineLectorWebFacade programDisciplineLectorWebFacade;

    private UUID lectorId;
    private UUID programId;
    private UUID disciplineId;
    private ProgramDisciplineLector entity;
    private ProgramDisciplineLectorId programDisciplineLectorId;
    private ProgramDisciplineLectorRequestDto request;
    private ProgramDisciplineLectorResponseDto response;

    @BeforeEach
    void setUp() {
        lectorId = UUID.randomUUID();
        programId = UUID.randomUUID();
        disciplineId = UUID.randomUUID();

        entity = new ProgramDisciplineLector();

        programDisciplineLectorId = new ProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);

        request = new ProgramDisciplineLectorRequestDto(
            lectorId,
            programId,
            disciplineId,
            LectorType.BOTH);

        response = new ProgramDisciplineLectorResponseDto(
            programDisciplineLectorId,
            lectorId,
            programId,
            disciplineId,
            LectorType.BOTH);
    }

    @Test
    void createProgramDisciplineLector_shouldReturnResponseDto_whenValidationPasses() {
        final ProgramDisciplineLector saved = new ProgramDisciplineLector();

        when(programDisciplineLectorMapper.toEntity(request)).thenReturn(entity);
        when(programDisciplineLectorService.save(entity)).thenReturn(saved);
        when(programDisciplineLectorMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.createProgramDisciplineLector(request);

        assertThat(result).isSameAs(response);

        final InOrder inOrder = inOrder(
            programDisciplineLectorValidator,
            programDisciplineLectorService
        );

        inOrder.verify(programDisciplineLectorValidator).validateForCreate(request);
        inOrder.verify(programDisciplineLectorService).save(entity);
    }

    @Test
    void createProgramDisciplineLector_shouldNotSave_whenValidationFails() {
        doThrow(new ResourceNotFoundException(LECTOR_NOT_FOUND.getMessage(String.valueOf(lectorId))))
            .when(programDisciplineLectorValidator).validateForCreate(request);

        assertThatThrownBy(() -> programDisciplineLectorWebFacade.createProgramDisciplineLector(
            request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorService, never()).save(any());
    }

    @Test
    void updateProgramDisciplineLector_shouldReturnResponseDto_whenExists() {
        final ProgramDisciplineLector saved = new ProgramDisciplineLector();

        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.of(
            entity));
        when(programDisciplineLectorService.save(entity)).thenReturn(saved);
        when(programDisciplineLectorMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.updateProgramDisciplineLector(
                lectorId, programId, disciplineId, request);

        assertThat(result).isSameAs(response);

        verify(programDisciplineLectorMapper).updateEntityFromDto(request, entity);
    }

    @Test
    void updateProgramDisciplineLector_shouldNotUpdate_whenRowDoesNotExist() {
        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> programDisciplineLectorWebFacade.updateProgramDisciplineLector(
            lectorId, programId, disciplineId, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorMapper, never()).updateEntityFromDto(any(), any());
        verify(programDisciplineLectorService, never()).save(any());
    }

    @Test
    void getAllProgramDisciplineLectors_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<ProgramDisciplineLector> entityList = List.of(entity);

        when(programDisciplineLectorService.getAll()).thenReturn(entityList);
        when(programDisciplineLectorMapper.toResponseDtoList(entityList)).thenReturn(List.of(
            response));

        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorWebFacade.getAllProgramDisciplineLectors();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getProgramDisciplineLectorById_shouldReturnResponseDto_whenRowExists() {
        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.of(
            entity));
        when(programDisciplineLectorMapper.toResponseDto(entity)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.getProgramDisciplineLectorById(
                lectorId, programId, disciplineId);

        assertThat(result).isSameAs(response);
    }

    @Test
    void getProgramDisciplineLectorById_shouldThrowNotFound_whenRowDoesNotExist() {
        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> programDisciplineLectorWebFacade.getProgramDisciplineLectorById(
            lectorId, programId, disciplineId))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorMapper, never()).toResponseDto(any());
    }

    @Test
    void deleteProgramDisciplineLector_shouldDelete_whenRowExists() {
        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.of(
            entity));

        programDisciplineLectorWebFacade.deleteProgramDisciplineLector(
            lectorId, programId, disciplineId);

        verify(programDisciplineLectorService).delete(entity);
    }

    @Test
    void deleteProgramDisciplineLector_shouldThrowNotFound_whenRowDoesNotExist() {
        when(programDisciplineLectorService.getById(programDisciplineLectorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> programDisciplineLectorWebFacade.deleteProgramDisciplineLector(
            lectorId, programId, disciplineId))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorService, never()).delete(any());
    }
}
