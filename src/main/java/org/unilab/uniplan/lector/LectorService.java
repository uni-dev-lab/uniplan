package org.unilab.uniplan.lector;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.faculty.FacultyService;
import org.unilab.uniplan.lector.dto.LectorDto;

@Service
@RequiredArgsConstructor
public class LectorService {
    public static final String LECTOR_NOT_FOUND = "Lector with ID {0} not found.";

    private final LectorRepository lectorRepository;

    private final LectorMapper lectorMapper;

    private final FacultyService facultyService;

    @Transactional
    public LectorDto createLector(LectorDto lectorDto) {
        final Lector lector = lectorMapper.toEntity(lectorDto);

        return saveEntityAndConvertToDto(lector);
    }

    public List<LectorDto> getAllLectors() {
        final List<Lector> lectors = lectorRepository.findAll();
        return lectorMapper.toDtos(lectors);
    }

    public Optional<LectorDto> getLectorById(UUID id) {
        return lectorRepository.findById(id).map(lectorMapper::toDto);
    }

    @Transactional
    public Optional<LectorDto> updateLector(UUID id, LectorDto lectorDto) {
        return lectorRepository.findById(id)
                               .map(existingLector -> updateEntityAndConvertToDto(
                                   lectorDto,
                                   existingLector));
    }

    public void deleteLector(UUID id) {
       final Lector lector =lectorRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException(
                                               MessageFormat.format(
                                                   LECTOR_NOT_FOUND,
                                                   id
                                               )
                                           ));
       lectorRepository.delete(lector);
    }

    private LectorDto updateEntityAndConvertToDto(final LectorDto dto,
                                                  final Lector entity) {
        lectorMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private LectorDto saveEntityAndConvertToDto(final Lector entity) {
        final Lector savedEntity = lectorRepository.save(entity);
        return lectorMapper.toDto(savedEntity);
    }
}

