package org.unilab.uniplan.major;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorService {

    private static final String MAJOR_NOT_FOUND = "Major with ID {0} not found.";

    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;

    @Transactional
    public MajorDTO createMajor(final MajorDTO majorDTO) {
        final Major major = majorMapper.toEntity(majorDTO);
        return majorMapper.toDTO(majorRepository.save(major));
    }

    public Optional<MajorDTO> findMajorById(final UUID id) {
        return majorRepository.findById(id)
                              .map(majorMapper::toDTO);
    }

    public List<MajorDTO> findAll() {
        return majorRepository.findAll()
                              .stream().map(majorMapper::toDTO).toList();
    }

    @Transactional
    public MajorDTO updateMajor(final UUID id, final MajorDTO majorDTO) {
        final Major major = majorRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException(
                                               MessageFormat.format(MAJOR_NOT_FOUND, id)));

        majorMapper.updateEntityFromDTO(majorDTO, major);
        return majorMapper.toDTO(majorRepository.save(major));
    }

    @Transactional
    public void deleteMajor(final UUID id) {
        final Major major = majorRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException(
                                               MessageFormat.format(MAJOR_NOT_FOUND, id)));
        majorRepository.delete(major);
    }
}
