package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class DisciplineWebFacadeTest {

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private DisciplineMapper disciplineMapper;

    @InjectMocks
    private DisciplineWebFacade disciplineWebFacade;

    @Test
    void createDiscipline_shouldReturnResponseDto_whenCalled() {
        final DisciplineRequestDto request = createRequest();
        final Discipline entity = mock(Discipline.class);
        final Discipline saved = mock(Discipline.class);
        final DisciplineResponseDto response = mock(DisciplineResponseDto.class);
        when(disciplineMapper.toEntity(request)).thenReturn(entity);
        when(disciplineService.save(entity)).thenReturn(saved);
        when(disciplineMapper.toResponseDto(saved)).thenReturn(response);

        final var result = disciplineWebFacade.createDiscipline(request);

        verify(disciplineMapper).toEntity(request);
        verify(disciplineService).save(entity);
        verify(disciplineMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateDiscipline_shouldReturnResponseDto_whenExists() {
        final UUID id = UUID.randomUUID();
        final DisciplineRequestDto request = createRequest();
        final Discipline entity = mock(Discipline.class);
        final Discipline saved = mock(Discipline.class);
        final DisciplineResponseDto response = mock(DisciplineResponseDto.class);
        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(disciplineService.save(entity)).thenReturn(saved);
        when(disciplineMapper.toResponseDto(saved)).thenReturn(response);

        final var result = disciplineWebFacade.updateDiscipline(id, request);

        final InOrder inOrder = inOrder(disciplineService, disciplineMapper);
        inOrder.verify(disciplineService).getById(id);
        inOrder.verify(disciplineMapper).updateEntityFromDto(request, entity);
        inOrder.verify(disciplineService).save(entity);
        inOrder.verify(disciplineMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateDiscipline_shouldNotUpdate_whenRowDoesNotExist() {
        final UUID id = UUID.randomUUID();
        final DisciplineRequestDto request = createRequest();

        assertThatThrownBy(() -> disciplineWebFacade.updateDiscipline(id, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineService).getById(id);
        verify(disciplineService, never()).save(any());
        verifyNoInteractions(disciplineMapper);
    }

    @Test
    void getAllDisciplines_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<Discipline> entities = mock();
        final List<DisciplineResponseDto> responses = mock();
        when(disciplineService.getAll()).thenReturn(entities);
        when(disciplineMapper.toResponseDtoList(entities)).thenReturn(responses);

        final var result = disciplineWebFacade.getAllDisciplines();

        verify(disciplineService).getAll();
        verify(disciplineMapper).toResponseDtoList(entities);
        assertThat(result).isSameAs(responses);
    }

    @Test
    void getDisciplineById_shouldReturnResponseDto_whenRowExists() {
        final UUID id = UUID.randomUUID();
        final Discipline entity = mock(Discipline.class);
        final DisciplineResponseDto response = mock(DisciplineResponseDto.class);
        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(disciplineMapper.toResponseDto(entity)).thenReturn(response);

        final var result = disciplineWebFacade.getDisciplineById(id);

        verify(disciplineService).getById(id);
        verify(disciplineMapper).toResponseDto(entity);
        assertThat(result).isSameAs(response);
    }

    @Test
    void getDisciplineById_shouldThrowNotFound_whenRowDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> disciplineWebFacade.getDisciplineById(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineService).getById(id);
        verifyNoInteractions(disciplineMapper);
    }

    @Test
    void deleteDiscipline_shouldDelete_whenRowExists() {
        final UUID id = UUID.randomUUID();
        final Discipline entity = mock(Discipline.class);
        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));

        disciplineWebFacade.deleteDiscipline(id);

        verify(disciplineService).getById(id);
        verify(disciplineService).delete(entity);
    }

    @Test
    void deleteDiscipline_shouldThrowNotFound_whenRowDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> disciplineWebFacade.deleteDiscipline(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineService).getById(id);
        verify(disciplineService, never()).delete(any());
    }

    private DisciplineRequestDto createRequest() {
        return new DisciplineRequestDto("name", "lector");
    }
}