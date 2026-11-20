package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.unilab.uniplan.lector.dto.LectorFilterDto;

@ExtendWith(MockitoExtension.class)
class LectorServiceTest {

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private LectorService lectorService;

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        final Lector lector = mock(Lector.class);
        final Lector saved = mock(Lector.class);
        when(lectorRepository.save(lector)).thenReturn(saved);

        final var result = lectorService.save(lector);

        verify(lectorRepository).save(lector);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void getAll_shouldReturnListOfEntities_whenCalled() {
        final List<Lector> lectors = mock();
        when(lectorRepository.findAll()).thenReturn(lectors);

        final var result = lectorService.getAll();

        verify(lectorRepository).findAll();
        assertThat(result).isSameAs(lectors);
    }

    @Test
    void getById_shouldReturnEntity_whenLectorExists() {
        final UUID id = UUID.randomUUID();
        final Lector lector = mock(Lector.class);
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));

        final var result = lectorService.getById(id);

        verify(lectorRepository).findById(id);
        assertThat(result).contains(lector);
    }

    @Test
    void getById_shouldReturnEmpty_whenLectorDoesNotExist() {
        final UUID id = UUID.randomUUID();

        final var result = lectorService.getById(id);

        verify(lectorRepository).findById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        final Lector lector = mock(Lector.class);

        lectorService.delete(lector);

        verify(lectorRepository).delete(lector);
    }

    @Test
    void getAll_shouldReturnRepositoryPage_whenFilterAndPageableGiven() {
        final PageRequest pageable = PageRequest.of(0, 10);
        final Lector entity = mock(Lector.class);
        final LectorFilterDto filter = new LectorFilterDto(
            null, null, null, null, null, null);
        final Page<Lector> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(lectorRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        final var result = lectorService.getAll(filter, pageable);

        verify(lectorRepository).findAll(any(Specification.class), eq(pageable));
        assertThat(result).isSameAs(page);
    }
}
