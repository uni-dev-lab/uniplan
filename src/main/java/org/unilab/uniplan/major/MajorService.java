package org.unilab.uniplan.major;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.major.dto.MajorDto;

@Service
@RequiredArgsConstructor
public class MajorService {

    private static final String MAJOR_NOT_FOUND = "Major with ID {0} not found.";

    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;

    @Transactional
    public MajorDto createMajor(final MajorDto majorDTO) {
        final Major major = majorMapper.toEntity(majorDTO);
        return saveEntityAndConvertToDto(major);
    }

    public Optional<MajorDto> findMajorById(final UUID id) {
        return majorRepository.findById(id)
                              .map(majorMapper::toDto);
    }

    public List<MajorDto> findAll() {
        return majorRepository.findAll()
                              .stream().map(majorMapper::toDto).toList();
    }

    @Transactional
    public Optional<MajorDto> updateMajor(final UUID id, final MajorDto majorDTO) {
        return majorRepository.findById(id).map(existingMajor -> updateEntityAndConvertToDto(
            majorDTO,
            existingMajor));
    }

    @Transactional
    public void deleteMajor(final UUID id) {
        final Major major = majorRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException(
                                               MessageFormat.format(MAJOR_NOT_FOUND, id)));
        majorRepository.delete(major);
    }

    private MajorDto updateEntityAndConvertToDto(final MajorDto dto,
                                                 final Major entity) {
        majorMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private MajorDto saveEntityAndConvertToDto(final Major entity) {
        final Major savedEntity = majorRepository.save(entity);
        return majorMapper.toDto(savedEntity);
    }
}
