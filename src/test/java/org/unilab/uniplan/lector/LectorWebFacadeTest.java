package org.unilab.uniplan.lector;

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
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@ExtendWith(MockitoExtension.class)
class LectorWebFacadeTest {

    @Mock
    private LectorService lectorService;

    @Mock
    private LectorValidator lectorValidator;

    @Mock
    private LectorMapper lectorMapper;

    @InjectMocks
    private LectorWebFacade lectorWebFacade;

    @Test
    void createLector_shouldReturnResponseDto_whenRequestIsValid() {
        final LectorRequestDto request = createRequest();
        final Lector entity = mock(Lector.class);
        final Lector saved = mock(Lector.class);
        final LectorResponseDto response = mock(LectorResponseDto.class);
        when(lectorMapper.toEntity(request)).thenReturn(entity);
        when(lectorService.save(entity)).thenReturn(saved);
        when(lectorMapper.toResponseDto(saved)).thenReturn(response);

        final var result = lectorWebFacade.createLector(request);

        final InOrder inOrder = inOrder(lectorValidator, lectorMapper, lectorService);
        inOrder.verify(lectorValidator).validate(request);
        inOrder.verify(lectorMapper).toEntity(request);
        inOrder.verify(lectorService).save(entity);
        inOrder.verify(lectorMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void createLector_shouldNotSave_whenValidationFails() {
        final LectorRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException("")).when(lectorValidator).validate(request);

        assertThatThrownBy(() -> lectorWebFacade.createLector(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(lectorValidator).validate(request);
        verifyNoInteractions(lectorMapper, lectorService);
    }

    @Test
    void getAllLectors_shouldReturnListOfResponseDtos_whenCalled() {
        final List<Lector> entities = mock();
        final List<LectorResponseDto> responses = mock();
        when(lectorService.getAll()).thenReturn(entities);
        when(lectorMapper.toResponseDtoList(entities)).thenReturn(responses);

        final var result = lectorWebFacade.getAllLectors();

        verify(lectorService).getAll();
        verify(lectorMapper).toResponseDtoList(entities);
        assertThat(result).isSameAs(responses);
    }

    @Test
    void getLectorById_shouldReturnResponseDto_whenLectorExists() {
        final UUID id = UUID.randomUUID();
        final Lector entity = mock(Lector.class);
        final LectorResponseDto response = mock(LectorResponseDto.class);
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));
        when(lectorMapper.toResponseDto(entity)).thenReturn(response);

        final var result = lectorWebFacade.getLectorById(id);

        verify(lectorService).getById(id);
        verify(lectorMapper).toResponseDto(entity);
        assertThat(result).isSameAs(response);
    }

    @Test
    void getLectorById_shouldThrowResourceNotFoundException_whenLectorDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> lectorWebFacade.getLectorById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());

        verify(lectorService).getById(id);
        verifyNoInteractions(lectorMapper);
    }

    @Test
    void updateLector_shouldReturnResponseDto_whenLectorExists() {
        final UUID id = UUID.randomUUID();
        final LectorRequestDto request = createRequest();
        final Lector entity = mock(Lector.class);
        final Lector saved = mock(Lector.class);
        final LectorResponseDto response = mock(LectorResponseDto.class);
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));
        when(lectorService.save(entity)).thenReturn(saved);
        when(lectorMapper.toResponseDto(saved)).thenReturn(response);

        final var result = lectorWebFacade.updateLector(id, request);

        final InOrder inOrder = inOrder(lectorService, lectorValidator, lectorMapper);
        inOrder.verify(lectorValidator).validate(request);
        inOrder.verify(lectorService).getById(id);
        inOrder.verify(lectorMapper).updateEntity(request, entity);
        inOrder.verify(lectorService).save(entity);
        inOrder.verify(lectorMapper).toResponseDto(saved);
        assertThat(result).isSameAs(response);
    }

    @Test
    void updateLector_shouldNotSave_whenValidationFails() {
        final UUID id = UUID.randomUUID();
        final LectorRequestDto request = createRequest();
        doThrow(new ResourceNotFoundException("")).when(lectorValidator).validate(request);

        assertThatThrownBy(() -> lectorWebFacade.updateLector(id, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(lectorValidator).validate(request);
        verifyNoInteractions(lectorMapper, lectorService);
    }

    @Test
    void updateLector_shouldThrowResourceNotFoundException_whenLectorDoesNotExist() {
        final UUID id = UUID.randomUUID();
        final LectorRequestDto request = createRequest();

        assertThatThrownBy(() -> lectorWebFacade.updateLector(id, request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());

        verify(lectorValidator).validate(request);
        verify(lectorService).getById(id);
        verify(lectorService, never()).save(any());
        verifyNoInteractions(lectorMapper);
    }

    @Test
    void deleteLectorById_shouldDelete_whenLectorExists() {
        final UUID id = UUID.randomUUID();
        final Lector entity = mock(Lector.class);
        when(lectorService.getById(id)).thenReturn(Optional.of(entity));

        lectorWebFacade.deleteLectorById(id);

        verify(lectorService).getById(id);
        verify(lectorService).delete(entity);
    }

    @Test
    void deleteLectorById_shouldThrowResourceNotFoundException_whenLectorDoesNotExist() {
        final UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> lectorWebFacade.deleteLectorById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());

        verify(lectorService).getById(id);
        verify(lectorService, never()).delete(any());
    }

    private LectorRequestDto createRequest() {
        return new LectorRequestDto(UUID.randomUUID(), "i.ivanov@gmail.com", "Ivan", "Ivanov");
    }
}
