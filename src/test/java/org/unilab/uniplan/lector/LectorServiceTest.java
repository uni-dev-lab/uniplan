package org.unilab.uniplan.lector;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class LectorServiceTest {

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private LectorService lectorService;

    private UUID id;
    private Lector lector;

    @BeforeEach
    void setUp(){
        id = UUID.randomUUID();
        lector = new Lector();
    }

    @Test
    void testSaveShouldSaveLector() {
        lectorService.save(lector);
        verify(lectorRepository).save(lector);
    }

    @Test
    void testGetAllLectorsShouldReturnListOfLectors() {
        final List<Lector> entities = List.of(lector);
        when(lectorRepository.findAll()).thenReturn(entities);

        final List<Lector> result = lectorService.getAll();

        assertEquals(entities, result);
        verify(lectorRepository).findAll();
    }

    @Test
    void testGetByIdShouldReturnLectorOptionalIfLectorNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));

        final Optional<Lector> result = lectorService.getById(id);

        assertEquals(Optional.of(lector), result);
        verify(lectorRepository).findById(id);
    }

    @Test
    void testGetByIdShouldReturnEmptyOptionalIfLectorNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<Lector> result = lectorService.getById(id);

        assertEquals(Optional.empty(), result);
        verify(lectorRepository).findById(id);
    }

    @Test
    void testDeleteShouldDeleteLector() {
        lectorService.delete(lector);

        verify(lectorRepository).delete(lector);
    }
}
