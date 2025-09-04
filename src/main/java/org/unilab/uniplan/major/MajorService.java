package org.unilab.uniplan.major;

import static org.unilab.uniplan.utils.ErrorConstants.MAJOR_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.major.dto.MajorDto;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;

    @Transactional
    public MajorDto createMajor(final MajorDto majorDTO) {
        final Major major = majorMapper.toEntity(majorDTO);
        return saveEntityAndConvertToDto(major);
    }

    public MajorDto findMajorById(final UUID id) {
        return majorRepository.findById(id)
                              .map(majorMapper::toDto)
                              .orElseThrow(() -> new ResourceNotFoundException(
                                  MAJOR_NOT_FOUND.getMessage(String.valueOf(id))));
    }

    public List<MajorDto> findAll() {
        return majorRepository.findAll()
                              .stream().map(majorMapper::toDto).toList();
    }

    public List<MajorDto> findAllMajorByFacultyId(final UUID facultyId) {
        return majorRepository.findAllByFacultyId(facultyId)
                              .stream()
                              .map(majorMapper::toDto)
                              .toList();
    }

    @Transactional
    public MajorDto updateMajor(final UUID id, final MajorDto majorDTO) {
        return majorRepository.findById(id).map(existingMajor -> updateEntityAndConvertToDto(
            majorDTO,
            existingMajor))
                              .orElseThrow(() -> new ResourceNotFoundException(MAJOR_NOT_FOUND.getMessage(
                                  String.valueOf(id))));
    }

    @Transactional
    public void deleteMajor(final UUID id) {
        final Major major = majorRepository.findById(id)
                                           .orElseThrow(() -> new ResourceNotFoundException(
                                               MAJOR_NOT_FOUND.getMessage(String.valueOf(id))));
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
