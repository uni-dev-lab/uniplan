package org.unilab.uniplan.lector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
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
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@ExtendWith(MockitoExtension.class)
class LectorWebFacadeTest {

    @Mock
    private LectorService lectorService;

    @Mock
    private LectorMapper lectorMapper;

    @Mock
    private LectorValidator lectorValidator;

    @InjectMocks
    private LectorWebFacade lectorWebFacade;

    private UUID id;
    private Lector lector;
    private LectorRequestDto requestDto;
    private LectorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        lector = new Lector();
        requestDto = mock(LectorRequestDto.class);
        responseDto = mock(LectorResponseDto.class);
    }

    @Test
    void testCreateLectorShouldValidateMapAndSaveLector() {
        when(lectorMapper.toEntity(requestDto)).thenReturn(lector);

        lectorWebFacade.createLector(requestDto);

        final InOrder inOrder = inOrder(lectorValidator, lectorMapper, lectorService);
        inOrder.verify(lectorValidator).validateForCreate(requestDto);
        inOrder.verify(lectorMapper).toEntity(requestDto);
        inOrder.verify(lectorService).save(lector);
    }

    @Test
    void testGetAllLectorsShouldReturnResponseDtoList() {
        final List<Lector> lectors = List.of(lector);
        final List<LectorResponseDto> responseDtos = List.of(responseDto);

        when(lectorService.getAll()).thenReturn(lectors);
        when(lectorMapper.toResponseDtoList(lectors)).thenReturn(responseDtos);

        final List<LectorResponseDto> result = lectorWebFacade.getAllLectors();

        assertEquals(responseDtos, result);
        verify(lectorService).getAll();
        verify(lectorMapper).toResponseDtoList(lectors);
    }

    @Test
    void testGetLectorByIdShouldReturnResponseDtoIfFound() {
        when(lectorService.getById(id)).thenReturn(Optional.of(lector));
        when(lectorMapper.toResponseDto(lector)).thenReturn(responseDto);

        final LectorResponseDto result = lectorWebFacade.getLectorById(id);

        assertEquals(responseDto, result);
        verify(lectorService).getById(id);
        verify(lectorMapper).toResponseDto(lector);
    }

    @Test
    void testGetLectorByIdShouldThrowIfNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> lectorWebFacade.getLectorById(id));

        verify(lectorService).getById(id);
        verify(lectorMapper, never()).toResponseDto(any(Lector.class));
    }

    @Test
    void testUpdateLectorShouldValidateUpdateAndSaveLectorIfFound() {
        when(lectorService.getById(id)).thenReturn(Optional.of(lector));

        lectorWebFacade.updateLector(id, requestDto);

        final InOrder inOrder = inOrder(lectorValidator, lectorService, lectorMapper);
        inOrder.verify(lectorValidator).validateForUpdate(requestDto);
        inOrder.verify(lectorService).getById(id);
        inOrder.verify(lectorMapper).updateEntityFromDto(requestDto, lector);
        inOrder.verify(lectorService).save(lector);
    }

    @Test
    void testUpdateLectorShouldThrowIfNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> lectorWebFacade.updateLector(id, requestDto));

        verify(lectorValidator).validateForUpdate(requestDto);
        verify(lectorService).getById(id);
        verify(lectorMapper, never()).updateEntityFromDto(any(LectorRequestDto.class), any(Lector.class));
        verify(lectorService, never()).save(any(Lector.class));
    }

    @Test
    void testDeleteLectorShouldDeleteLectorIfFound() {
        when(lectorService.getById(id)).thenReturn(Optional.of(lector));

        lectorWebFacade.deleteLector(id);

        verify(lectorService).getById(id);
        verify(lectorService).delete(lector);
    }

    @Test
    void testDeleteLectorShouldThrowIfNotFound() {
        when(lectorService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> lectorWebFacade.deleteLector(id));

        verify(lectorService).getById(id);
        verify(lectorService, never()).delete(any(Lector.class));
    }
}