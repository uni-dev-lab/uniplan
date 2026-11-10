package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Test
    void createProgramDisciplineLector_shouldReturnResponseDto_whenValidationPasses() {
        final ProgramDisciplineLectorRequestDto request = createRequest();
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLector saved = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLectorResponseDto response =
            mock(ProgramDisciplineLectorResponseDto.class);
        when(programDisciplineLectorMapper.toEntity(request)).thenReturn(entity);
        when(programDisciplineLectorService.save(entity)).thenReturn(saved);
        when(programDisciplineLectorMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.createProgramDisciplineLector(request);

        final InOrder inOrder = inOrder(programDisciplineLectorValidator,
                                        programDisciplineLectorMapper,
                                        programDisciplineLectorService);
        inOrder.verify(programDisciplineLectorValidator).validateForCreate(request);
        inOrder.verify(programDisciplineLectorMapper).toEntity(request);
        inOrder.verify(programDisciplineLectorService).save(entity);
        inOrder.verify(programDisciplineLectorMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void createProgramDisciplineLector_shouldNotSave_whenValidationFails() {
        final ProgramDisciplineLectorRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException(""))
            .when(programDisciplineLectorValidator).validateForCreate(request);

        assertThatThrownBy(
            () -> programDisciplineLectorWebFacade.createProgramDisciplineLector(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorValidator).validateForCreate(request);
        verifyNoInteractions(programDisciplineLectorMapper, programDisciplineLectorService);
    }

    @Test
    void updateProgramDisciplineLector_shouldReturnResponseDto_whenExists() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLectorRequestDto request = createRequest();
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLector saved = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLectorResponseDto response =
            mock(ProgramDisciplineLectorResponseDto.class);
        when(programDisciplineLectorService.getById(id)).thenReturn(Optional.of(entity));
        when(programDisciplineLectorService.save(entity)).thenReturn(saved);
        when(programDisciplineLectorMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.updateProgramDisciplineLector(id.getLectorId(),
                                                                           id.getProgramId(),
                                                                           id.getDisciplineId(),
                                                                           request);

        final InOrder inOrder = inOrder(programDisciplineLectorService,
                                        programDisciplineLectorMapper);
        inOrder.verify(programDisciplineLectorService).getById(id);
        inOrder.verify(programDisciplineLectorMapper).updateEntityFromDto(request, entity);
        inOrder.verify(programDisciplineLectorService).save(entity);
        inOrder.verify(programDisciplineLectorMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateProgramDisciplineLector_shouldNotUpdate_whenRowDoesNotExist() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLectorRequestDto request = createRequest();

        assertThatThrownBy(
            () -> programDisciplineLectorWebFacade.updateProgramDisciplineLector(id.getLectorId(),
                                                                                 id.getProgramId(),
                                                                                 id.getDisciplineId(),
                                                                                 request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorService).getById(id);
        verify(programDisciplineLectorService, never()).save(any());
        verifyNoInteractions(programDisciplineLectorMapper);
    }

    @Test
    void getAllProgramDisciplineLectors_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<ProgramDisciplineLector> entities = mock();
        final List<ProgramDisciplineLectorResponseDto> responses = mock();
        when(programDisciplineLectorService.getAll()).thenReturn(entities);
        when(programDisciplineLectorMapper.toResponseDtoList(entities)).thenReturn(responses);

        final List<ProgramDisciplineLectorResponseDto> result =
            programDisciplineLectorWebFacade.getAllProgramDisciplineLectors();

        verify(programDisciplineLectorService).getAll();
        verify(programDisciplineLectorMapper).toResponseDtoList(entities);
        assertThat(result).isSameAs(responses);
    }

    @Test
    void getProgramDisciplineLectorById_shouldReturnResponseDto_whenRowExists() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLectorResponseDto response =
            mock(ProgramDisciplineLectorResponseDto.class);
        when(programDisciplineLectorService.getById(id)).thenReturn(Optional.of(entity));
        when(programDisciplineLectorMapper.toResponseDto(entity)).thenReturn(response);

        final ProgramDisciplineLectorResponseDto result =
            programDisciplineLectorWebFacade.getProgramDisciplineLectorById(id.getLectorId(),
                                                                            id.getProgramId(),
                                                                            id.getDisciplineId());

        verify(programDisciplineLectorService).getById(id);
        verify(programDisciplineLectorMapper).toResponseDto(entity);
        assertThat(result).isSameAs(response);
    }

    @Test
    void getProgramDisciplineLectorById_shouldThrowNotFound_whenRowDoesNotExist() {
        final ProgramDisciplineLectorId id = createId();

        assertThatThrownBy(
            () -> programDisciplineLectorWebFacade.getProgramDisciplineLectorById(id.getLectorId(),
                                                                                  id.getProgramId(),
                                                                                  id.getDisciplineId()))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorService).getById(id);
        verifyNoInteractions(programDisciplineLectorMapper);
    }

    @Test
    void deleteProgramDisciplineLector_shouldDelete_whenRowExists() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        when(programDisciplineLectorService.getById(id)).thenReturn(Optional.of(entity));

        programDisciplineLectorWebFacade.deleteProgramDisciplineLector(id.getLectorId(),
                                                                       id.getProgramId(),
                                                                       id.getDisciplineId());

        verify(programDisciplineLectorService).getById(id);
        verify(programDisciplineLectorService).delete(entity);
    }

    @Test
    void deleteProgramDisciplineLector_shouldThrowNotFound_whenRowDoesNotExist() {
        final ProgramDisciplineLectorId id = createId();

        assertThatThrownBy(
            () -> programDisciplineLectorWebFacade.deleteProgramDisciplineLector(id.getLectorId(),
                                                                                 id.getProgramId(),
                                                                                 id.getDisciplineId()))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineLectorService).getById(id);
        verify(programDisciplineLectorService, never()).delete(any());
    }

    private ProgramDisciplineLectorId createId() {
        return new ProgramDisciplineLectorId(UUID.randomUUID(),
                                             UUID.randomUUID(),
                                             UUID.randomUUID());
    }

    private ProgramDisciplineLectorRequestDto createRequest() {
        return new ProgramDisciplineLectorRequestDto(UUID.randomUUID(),
                                                     UUID.randomUUID(),
                                                     UUID.randomUUID(),
                                                     LectorType.BOTH);
    }
}