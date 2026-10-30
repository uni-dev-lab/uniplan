package org.unilab.uniplan.program;

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
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;

@ExtendWith(MockitoExtension.class)
class ProgramWebFacadeTest {

    @Mock
    private ProgramService programService;

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private ProgramValidator programValidator;

    @InjectMocks
    private ProgramWebFacade programWebFacade;

    @Test
    void createProgram_shouldReturnResponseDto_whenValidationPasses() {
        final ProgramRequestDto request = createRequest();
        final Program entity = mock(Program.class);
        final Program saved = mock(Program.class);
        final ProgramResponseDto response = mock(ProgramResponseDto.class);
        when(programMapper.toEntity(request)).thenReturn(entity);
        when(programService.save(entity)).thenReturn(saved);
        when(programMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.createProgram(request);

        final InOrder inOrder = inOrder(programValidator, programMapper, programService);
        inOrder.verify(programValidator).validateForCreate(request);
        inOrder.verify(programMapper).toEntity(request);
        inOrder.verify(programService).save(entity);
        inOrder.verify(programMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void createProgram_shouldNotSave_whenValidationFails() {
        final ProgramRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException(""))
            .when(programValidator).validateForCreate(request);

        assertThatThrownBy(() -> programWebFacade.createProgram(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programValidator).validateForCreate(request);
        verifyNoInteractions(programMapper, programService);
    }

    @Test
    void updateProgram_shouldReturnResponseDto_whenValid() {
        final UUID id = UUID.randomUUID();
        final ProgramRequestDto request = createRequest();
        final Program entity = mock(Program.class);
        final Program saved = mock(Program.class);
        final ProgramResponseDto response = mock(ProgramResponseDto.class);
        when(programService.getById(id)).thenReturn(Optional.of(entity));
        when(programService.save(entity)).thenReturn(saved);
        when(programMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.updateProgram(id, request);

        final InOrder inOrder = inOrder(programValidator, programService, programMapper);
        inOrder.verify(programValidator).validateForUpdate(id, request);
        inOrder.verify(programService).getById(id);
        inOrder.verify(programMapper).updateEntityFromDto(request, entity);
        inOrder.verify(programService).save(entity);
        inOrder.verify(programMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateProgram_shouldNotUpdate_whenValidationFails() {
        final UUID id = UUID.randomUUID();
        final ProgramRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException(""))
            .when(programValidator).validateForUpdate(id, request);

        assertThatThrownBy(() -> programWebFacade.updateProgram(id, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programValidator).validateForUpdate(id, request);
        verifyNoInteractions(programService, programMapper);
    }

    @Test
    void getAllPrograms_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<Program> entities = mock();
        final List<ProgramResponseDto> responses = mock();
        when(programService.getAll()).thenReturn(entities);
        when(programMapper.toResponseDtoList(entities)).thenReturn(responses);

        final List<ProgramResponseDto> result = programWebFacade.getAllPrograms();

        verify(programService).getAll();
        verify(programMapper).toResponseDtoList(entities);
        assertThat(result).isSameAs(responses);
    }

    @Test
    void getProgramById_shouldReturnResponseDto_whenRowExists() {
        final UUID id = UUID.randomUUID();
        final Program entity = mock(Program.class);
        final ProgramResponseDto response = mock(ProgramResponseDto.class);
        when(programService.getById(id)).thenReturn(Optional.of(entity));
        when(programMapper.toResponseDto(entity)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.getProgramById(id);

        verify(programService).getById(id);
        verify(programMapper).toResponseDto(entity);
        assertThat(result).isSameAs(response);
    }

    @Test
    void getProgramById_shouldThrowNotFound_whenRowDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> programWebFacade.getProgramById(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programService).getById(id);
        verifyNoInteractions(programMapper);
    }

    @Test
    void deleteProgram_shouldDelete_whenRowExists() {
        final UUID id = UUID.randomUUID();
        final Program entity = mock(Program.class);
        when(programService.getById(id)).thenReturn(Optional.of(entity));

        programWebFacade.deleteProgram(id);

        verify(programService).getById(id);
        verify(programService).delete(entity);
    }

    @Test
    void deleteProgram_shouldThrowNotFound_whenRowDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> programWebFacade.deleteProgram(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programService).getById(id);
        verify(programService, never()).delete(any());
    }

    private ProgramRequestDto createRequest() {
        return new ProgramRequestDto(UUID.randomUUID());
    }
}