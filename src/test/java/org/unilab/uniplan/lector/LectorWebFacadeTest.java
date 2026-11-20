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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.unilab.uniplan.common.specification.SortValidator;
import org.unilab.uniplan.exception.InvalidSortPropertyException;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.lector.dto.LectorFilterDto;
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

    @Mock
    private SortValidator sortValidator;

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
    void getAllLectors_shouldReturnMappedPage_whenSortIsValid() {
        final PageRequest pageable = PageRequest.of(0, 10);
        final LectorFilterDto filter = new LectorFilterDto(
            null, null, null, null, null, null);
        final Lector entity = mock(Lector.class);
        final LectorResponseDto response = mock(LectorResponseDto.class);
        final Page<Lector> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(lectorService.getAll(filter, pageable)).thenReturn(page);
        when(lectorMapper.toResponseDto(entity)).thenReturn(response);

        final var result = lectorWebFacade.getAllLectors(filter, pageable);

        final InOrder inOrder = inOrder(sortValidator, lectorService, lectorMapper);
        inOrder.verify(sortValidator).validate(
            pageable, LectorSpecifications.ALLOWED_PROPERTIES);
        inOrder.verify(lectorService).getAll(filter, pageable);
        inOrder.verify(lectorMapper).toResponseDto(entity);
        assertThat(result.content()).containsExactly(response);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void getAllLectors_shouldNotQueryService_whenSortIsInvalid() {
        final PageRequest pageable = PageRequest.of(0, 10);
        final LectorFilterDto filter = new LectorFilterDto(
            null, null, null, null, null, null);
        doThrow(new InvalidSortPropertyException(""))
            .when(sortValidator).validate(pageable, LectorSpecifications.ALLOWED_PROPERTIES);

        assertThatThrownBy(() -> lectorWebFacade.getAllLectors(filter, pageable))
            .isInstanceOf(InvalidSortPropertyException.class);

        verify(sortValidator).validate(pageable, LectorSpecifications.ALLOWED_PROPERTIES);
        verifyNoInteractions(lectorService, lectorMapper);
    }

    @Test
    void getAllLectors_shouldReturnEmptyPage_whenNoLectorsMatch() {
        final PageRequest pageable = PageRequest.of(0, 10);
        final LectorFilterDto filter = new LectorFilterDto(
            null, null, null, null, null, null);
        when(lectorService.getAll(filter, pageable)).thenReturn(Page.empty(pageable));

        final var result = lectorWebFacade.getAllLectors(filter, pageable);

        verify(sortValidator).validate(pageable, LectorSpecifications.ALLOWED_PROPERTIES);
        verify(lectorService).getAll(filter, pageable);
        verifyNoInteractions(lectorMapper);
        assertThat(result.content()).isEmpty();
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
