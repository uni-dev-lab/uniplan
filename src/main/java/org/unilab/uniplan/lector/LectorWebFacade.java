package org.unilab.uniplan.lector;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.LECTOR_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class LectorWebFacade {

    private final LectorMapper lectorMapper;
    private final LectorService lectorService;
    private final LectorValidator lectorValidator;

    @Transactional
    public void createLector(final LectorRequestDto request) {
        final Lector lector = lectorMapper.toEntity(request);
        lectorValidator.validate(lector);
        lectorService.save(lector);
        log.info("Lector with id {} has been created", lector.getId());
    }

    @Transactional(readOnly = true)
    public List<LectorResponseDto> getAllLectors() {
        return lectorMapper.toResponseDtoList(lectorService.getAll());
    }

    @Transactional
    public LectorResponseDto getLectorById(final UUID id) {
        final Lector lector = getLectorOrThrow(id);
        return lectorMapper.toResponseDto(lector);
    }

    private Lector getLectorOrThrow(final UUID id) {
        return lectorService.getById(id)
            .orElseThrow(()-> new ResourceNotFoundException(
                LECTOR_NOT_FOUND.getMessage(String.valueOf(id))
            ));
    }

    @Transactional
    public void updateLector(final UUID id,
                             final LectorRequestDto request) {
        final Lector lector = getLectorOrThrow(id);
        lectorMapper.updateEntity(request, lector);
        lectorValidator.validate(lector);
        lectorService.save(lector);
        log.info("Lector with id {} has been updated", lector.getId());
    }

    @Transactional
    public void deleteLectorById(final UUID id) {
        final Lector lector = getLectorOrThrow(id);
        lectorService.delete(lector);
        log.info("Lector with id {} has been deleted", lector.getId());
    }
}
