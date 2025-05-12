package org.unilab.uniplan.major;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;
    
    @Transactional
    public MajorDTO createMajor(final MajorDTO majorDTO) {
        final Major major = majorMapper.toEntity(majorDTO);
        return majorMapper.toDTO(majorRepository.save(major));
    }

    public Optional<MajorDTO> findMajorById (final UUID id) {
        return majorRepository.findById(id)
                               .map(majorMapper::toDTO);
    }
    
    public List<MajorDTO> findAll () {
        return majorRepository.findAll()
            .stream().map(majorMapper::toDTO).toList();
    }
    @Transactional
    public MajorDTO updateMajor (final UUID id, final MajorDTO majorDTO) {
        final Major major = majorRepository.findById(id)
                                  .orElseThrow(()->new MajorNotFoundException(id));
        majorMapper.updateEntityFromDTO(majorDTO, major);
        return majorMapper.toDTO(majorRepository.save(major));
    }
    @Transactional
    public boolean deleteMajor (final UUID id){
        if (majorRepository.existsById(id)) {
            majorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
