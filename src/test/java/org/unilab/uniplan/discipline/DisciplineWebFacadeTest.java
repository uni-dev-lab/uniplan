package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

@ExtendWith(MockitoExtension.class)
class DisciplineWebFacadeTest {

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private DisciplineMapper disciplineMapper;

    @InjectMocks
    private DisciplineWebFacade disciplineWebFacade;

    private UUID id;
    private Discipline entity;
    private DisciplineRequestDto request;
    private DisciplineResponseDto response;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        entity = new Discipline();

        request = new DisciplineRequestDto(
            "name",
            "lector");

        response = new DisciplineResponseDto(
            id,
            "name",
            "lector",
            List.of(new ProgramDisciplineId(UUID.randomUUID(), UUID.randomUUID()))
        );
    }

    @Test
    void createDiscipline_shouldReturnResponseDto_whenCalled() {
        final Discipline saved = new Discipline();

        when(disciplineMapper.toEntity(request)).thenReturn(entity);
        when(disciplineService.save(entity)).thenReturn(saved);
        when(disciplineMapper.toResponseDto(saved)).thenReturn(response);

        final var result = disciplineWebFacade.createDiscipline(request);

        assertThat(result).isSameAs(response);
    }

    @Test
    void updateDiscipline_shouldReturnResponseDto_whenExists() {
        final Discipline saved = new Discipline();

        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(disciplineService.save(entity)).thenReturn(saved);
        when(disciplineMapper.toResponseDto(saved)).thenReturn(response);

        final var result = disciplineWebFacade.updateDiscipline(id, request);

        assertThat(result).isSameAs(response);

        verify(disciplineMapper).updateEntityFromDto(request, entity);
    }

    @Test
    void updateDiscipline_shouldNotUpdate_whenRowDoesNotExist() {
        when(disciplineService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> disciplineWebFacade.updateDiscipline(id, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineMapper, never()).updateEntityFromDto(any(), any());
        verify(disciplineService, never()).save(any());
    }

    @Test
    void getAllDisciplines_shouldReturnListOfDTOs_whenThereAreEntities() {
        final List<Discipline> entityList = List.of(entity);

        when(disciplineService.getAll()).thenReturn(entityList);
        when(disciplineMapper.toResponseDtoList(entityList)).thenReturn(List.of(response));

        final var result = disciplineWebFacade.getAllDisciplines();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getDisciplineById_shouldReturnResponseDto_whenRowExists() {
        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));
        when(disciplineMapper.toResponseDto(entity)).thenReturn(response);

        final var result = disciplineWebFacade.getDisciplineById(id);

        assertThat(result).isSameAs(response);
    }

    @Test
    void getDisciplineById_shouldThrowNotFound_whenRowDoesNotExist() {
        when(disciplineService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> disciplineWebFacade.getDisciplineById(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineMapper, never()).toResponseDto(any());
    }

    @Test
    void deleteDiscipline_shouldDelete_whenRowExists() {
        when(disciplineService.getById(id)).thenReturn(Optional.of(entity));

        disciplineWebFacade.deleteDiscipline(id);

        verify(disciplineService).delete(entity);
    }

    @Test
    void deleteDiscipline_shouldThrowNotFound_whenRowDoesNotExist() {
        when(disciplineService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> disciplineWebFacade.deleteDiscipline(id))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(disciplineService, never()).delete(any());
    }
}
