package org.unilab.uniplan.program;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private UUID id;
    private Program entity;
    private ProgramRequestDto request;
    private ProgramResponseDto response;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        final UUID courseId = UUID.randomUUID();

        entity = new Program();

        request = new ProgramRequestDto(courseId);

        response = new ProgramResponseDto(
            id,
            courseId,
            List.of()
        );
    }

    @Test
    void createProgram_shouldReturnResponseDto_whenValidationPasses() {
        final Program saved = new Program();

        when(programMapper.toEntity(request)).thenReturn(entity);
        when(programService.save(entity)).thenReturn(saved);
        when(programMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.createProgram(request);

        assertThat(result).isSameAs(response);

        final InOrder inOrder = inOrder(programValidator, programService);

        inOrder.verify(programValidator).validateForCreate(request);
        inOrder.verify(programService).save(entity);
    }

    @Test
    void createProgram_shouldNotSave_whenValidationFails() {
        doThrow(new ResourceNotFoundException(""))
            .when(programValidator).validateForCreate(request);

        assertThatThrownBy(() -> programWebFacade.createProgram(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programService, never()).save(any());
    }

    @Test
    void updateProgram_shouldReturnResponseDto_whenValid() {
        final Program saved = new Program();

        when(programService.getById(id)).thenReturn(Optional.of(entity));
        when(programService.save(entity)).thenReturn(saved);
        when(programMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.updateProgram(id, request);

        assertThat(result).isSameAs(response);

        final InOrder inOrder = inOrder(programValidator, programService);

        inOrder.verify(programValidator).validateForUpdate(id, request);
        inOrder.verify(programService).getById(id);
        inOrder.verify(programService).save(entity);
        verify(programMapper).updateEntityFromDto(request, entity);
    }

    @Test
    void updateProgram_shouldNotUpdate_whenValidationFails() {
        doThrow(new ResourceNotFoundException(""))
            .when(programValidator).validateForUpdate(id, request);

        assertThatThrownBy(() -> programWebFacade.updateProgram(id, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programService, never()).save(any());
    }

    @Test
    void getAllPrograms_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<Program> entityList = List.of(entity);

        when(programService.getAll()).thenReturn(entityList);
        when(programMapper.toResponseDtoList(entityList)).thenReturn(List.of(response));

        final List<ProgramResponseDto> result = programWebFacade.getAllPrograms();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getProgramById_shouldReturnResponseDto_whenRowExists() {
        when(programService.getById(id)).thenReturn(Optional.of(entity));
        when(programMapper.toResponseDto(entity)).thenReturn(response);

        final ProgramResponseDto result = programWebFacade.getProgramById(id);

        assertThat(result).isSameAs(response);
    }

    @Test
    void getProgramById_shouldThrowNotFound_whenRowDoesNotExist() {
        when(programService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> programWebFacade.getProgramById(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programMapper, never()).toResponseDto(any());
    }

    @Test
    void deleteProgram_shouldDelete_whenRowExists() {
        when(programService.getById(id)).thenReturn(Optional.of(entity));

        programWebFacade.deleteProgram(id);

        verify(programService).delete(entity);
    }

    @Test
    void deleteProgram_shouldThrowNotFound_whenRowDoesNotExist() {
        when(programService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> programWebFacade.deleteProgram(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programService, never()).delete(any());
    }
}
