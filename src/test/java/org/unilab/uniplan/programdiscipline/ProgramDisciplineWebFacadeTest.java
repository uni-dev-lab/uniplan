package org.unilab.uniplan.programdiscipline;

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
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineWebFacadeTest {

    @Mock
    private ProgramDisciplineService programDisciplineService;

    @Mock
    private ProgramDisciplineMapper programDisciplineMapper;

    @Mock
    private ProgramDisciplineValidator programDisciplineValidator;

    @InjectMocks
    private ProgramDisciplineWebFacade programDisciplineWebFacade;

    @Test
    void createProgramDiscipline_shouldReturnResponseDto_whenValidationPasses() {
        final ProgramDisciplineRequestDto request = createRequest();
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        final ProgramDiscipline saved = mock(ProgramDiscipline.class);
        final ProgramDisciplineResponseDto response =
            mock(ProgramDisciplineResponseDto.class);

        when(programDisciplineMapper.toEntity(request)).thenReturn(entity);
        when(programDisciplineService.save(entity)).thenReturn(saved);
        when(programDisciplineMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineResponseDto result =
            programDisciplineWebFacade.createProgramDiscipline(request);

        final InOrder inOrder = inOrder(
            programDisciplineValidator,
            programDisciplineMapper,
            programDisciplineService);

        inOrder.verify(programDisciplineValidator).validateForCreate(request);
        inOrder.verify(programDisciplineMapper).toEntity(request);
        inOrder.verify(programDisciplineService).save(entity);
        inOrder.verify(programDisciplineMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void createProgramDiscipline_shouldNotSave_whenValidationFails() {
        final ProgramDisciplineRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException(""))
            .when(programDisciplineValidator).validateForCreate(request);

        assertThatThrownBy(
            () -> programDisciplineWebFacade.createProgramDiscipline(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineValidator).validateForCreate(request);
        verifyNoInteractions(programDisciplineMapper, programDisciplineService);
    }

    @Test
    void updateProgramDiscipline_shouldReturnResponseDto_whenExists() {
        final ProgramDisciplineId id = createId();
        final ProgramDisciplineRequestDto request = createRequest();
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        final ProgramDiscipline saved = mock(ProgramDiscipline.class);
        final ProgramDisciplineResponseDto response =
            mock(ProgramDisciplineResponseDto.class);

        when(programDisciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(programDisciplineService.save(entity)).thenReturn(saved);
        when(programDisciplineMapper.toResponseDto(saved)).thenReturn(response);

        final ProgramDisciplineResponseDto result =
            programDisciplineWebFacade.updateProgramDiscipline(
                id.getDisciplineId(), id.getProgramId(), request);

        final InOrder inOrder = inOrder(
            programDisciplineService, programDisciplineMapper);

        inOrder.verify(programDisciplineService).getById(id);
        inOrder.verify(programDisciplineMapper).updateEntityFromDto(request, entity);
        inOrder.verify(programDisciplineService).save(entity);
        inOrder.verify(programDisciplineMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateProgramDiscipline_shouldNotUpdate_whenRowDoesNotExist() {
        final ProgramDisciplineId id = createId();
        final ProgramDisciplineRequestDto request = createRequest();

        assertThatThrownBy(
            () -> programDisciplineWebFacade
                .updateProgramDiscipline(
                    id.getDisciplineId(), id.getProgramId(), request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineService).getById(id);
        verify(programDisciplineService, never()).save(any());
        verifyNoInteractions(programDisciplineMapper);
    }

    @Test
    void getAllProgramDisciplines_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<ProgramDiscipline> entities = mock();
        final List<ProgramDisciplineResponseDto> responses = mock();

        when(programDisciplineService.getAll()).thenReturn(entities);
        when(programDisciplineMapper.toResponseDtoList(entities)).thenReturn(responses);

        final List<ProgramDisciplineResponseDto> result =
            programDisciplineWebFacade.getAllProgramDisciplines();

        verify(programDisciplineService).getAll();
        verify(programDisciplineMapper).toResponseDtoList(entities);
        assertThat(result).isSameAs(responses);
    }

    @Test
    void getProgramDisciplineById_shouldReturnResponseDto_whenRowExists() {
        final ProgramDisciplineId id = createId();
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        final ProgramDisciplineResponseDto response =
            mock(ProgramDisciplineResponseDto.class);
        when(programDisciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(programDisciplineMapper.toResponseDto(entity)).thenReturn(response);

        final ProgramDisciplineResponseDto result =
            programDisciplineWebFacade.getProgramDisciplineById(
                id.getDisciplineId(),
                id.getProgramId());

        verify(programDisciplineService).getById(id);
        verify(programDisciplineMapper).toResponseDto(entity);
        assertThat(result).isSameAs(response);
    }

    @Test
    void getProgramDisciplineById_shouldThrowNotFound_whenRowDoesNotExist() {
        final ProgramDisciplineId id = createId();

        assertThatThrownBy(
            () -> programDisciplineWebFacade.getProgramDisciplineById(
                id.getDisciplineId(),
                id.getProgramId()))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineService).getById(id);
        verifyNoInteractions(programDisciplineMapper);
    }

    @Test
    void deleteProgramDiscipline_shouldDelete_whenRowExists() {
        final ProgramDisciplineId id = createId();
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        when(programDisciplineService.getById(id)).thenReturn(Optional.of(entity));
        programDisciplineWebFacade.deleteProgramDiscipline(
            id.getDisciplineId(),
            id.getProgramId());

        verify(programDisciplineService).getById(id);
        verify(programDisciplineService).delete(entity);
    }

    @Test
    void deleteProgramDiscipline_shouldThrowNotFound_whenRowDoesNotExist() {
        final ProgramDisciplineId id = createId();

        assertThatThrownBy(
            () -> programDisciplineWebFacade.deleteProgramDiscipline(
                id.getDisciplineId(),
                id.getProgramId()))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(programDisciplineService).getById(id);
        verify(programDisciplineService, never()).delete(any());
    }

    private ProgramDisciplineId createId() {
        return new ProgramDisciplineId(
            UUID.randomUUID(), UUID.randomUUID()
        );
    }

    private ProgramDisciplineRequestDto createRequest() {
        return new ProgramDisciplineRequestDto(
            UUID.randomUUID(),
            UUID.randomUUID(),
            (short) 1,
            (short) 1,
            (byte) 1
        );
    }
}
