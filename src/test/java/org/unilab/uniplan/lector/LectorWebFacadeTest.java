package org.unilab.uniplan.lector;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@ExtendWith(MockitoExtension.class)
class LectorWebFacadeTest {

    @Mock
    private LectorMapper lectorMapper;

    @Mock
    private LectorService lectorService;

    @Mock
    private LectorValidator lectorValidator;

    @InjectMocks
    private LectorWebFacade lectorWebFacade;

    private UUID id;
    private Lector entity;
    private LectorRequestDto requestDto;
    private LectorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        final UUID facultyId = UUID.randomUUID();

        entity = new Lector();
        requestDto = new LectorRequestDto(facultyId, "i.ivanov@gmail.com", "Ivan", "Ivanov");
        responseDto = new LectorResponseDto(id, facultyId, "i.ivanov@gmail.com", "Ivan", "Ivanov");
    }

    @Test
    void createLector_shouldValidateThenSaveLector_whenRequestIsValid() {
        when(lectorMapper.toEntity(requestDto)).thenReturn(entity);

        lectorWebFacade.createLector(requestDto);

        final var inOrder = inOrder(lectorMapper, lectorValidator, lectorService);

        inOrder.verify(lectorMapper).toEntity(requestDto);
        inOrder.verify(lectorValidator).validate(requestDto);
        inOrder.verify(lectorService).save(entity);
    }

    @Test
    void createLector_shouldNotSaveLector_whenValidationFails() {
        when(lectorMapper.toEntity(requestDto)).thenReturn(entity);
        doThrow(new ResourceNotFoundException("faculty missing"))
            .when(lectorValidator).validate(requestDto);

        assertThatThrownBy(() -> lectorWebFacade.createLector(requestDto))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(lectorService, never()).save(entity);
    }

    @Test
    void getAllLectors_shouldReturnListOfResponseDtos() {
        final List<Lector> entities = List.of(entity);
        when(lectorService.getAll()).thenReturn(entities);
        when(lectorMapper.toResponseDtoList(entities)).thenReturn(List.of(responseDto));

        final List<LectorResponseDto> result = lectorWebFacade.getAllLectors();

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getLectorById_shouldReturnResponseDto_whenLectorExists() {
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));
        when(lectorMapper.toResponseDto(entity)).thenReturn(responseDto);

        final LectorResponseDto result = lectorWebFacade.getLectorById(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getLectorById_shouldThrowResourceNotFoundException_whenLectorNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lectorWebFacade.getLectorById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void updateLector_shouldUpdateThenValidateThenSave_whenLectorExists() {
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));

        lectorWebFacade.updateLector(id, requestDto);

        final var inOrder = inOrder(lectorMapper, lectorValidator, lectorService);

        inOrder.verify(lectorMapper).updateEntity(requestDto, entity);
        inOrder.verify(lectorValidator).validate(requestDto);
        inOrder.verify(lectorService).save(entity);
    }

    @Test
    void updateLector_shouldNotSaveLector_whenValidationFails() {
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));
        doThrow(new ResourceNotFoundException("faculty missing"))
            .when(lectorValidator).validate(requestDto);

        assertThatThrownBy(() -> lectorWebFacade.updateLector(id, requestDto))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(lectorService, never()).save(entity);
    }

    @Test
    void updateLector_shouldThrowResourceNotFoundException_whenLectorNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lectorWebFacade.updateLector(id, requestDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void deleteLectorById_shouldDeleteLector_whenLectorExists() {
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));

        lectorWebFacade.deleteLectorById(id);

        verify(lectorService).delete(entity);
    }

    @Test
    void deleteLectorById_shouldThrowResourceNotFoundException_whenLectorNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lectorWebFacade.deleteLectorById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }
}
