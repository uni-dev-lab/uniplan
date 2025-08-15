package org.unilab.uniplan.university;

import static org.unilab.uniplan.utils.ErrorConstants.UNIVERSITY_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.dto.UniversityDto;

@Service
@RequiredArgsConstructor
public class UniversityService {


    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;

    @Transactional
    public UniversityDto createUniversity(final UniversityDto universityDto) {
        final University university = universityMapper.toEntity(universityDto);

        return saveEntityAndConvertToDto(university);
    }

    public List<UniversityDto> getAllUniversities() {
        final List<University> universities = universityRepository.findAll();

        return universityMapper.toDtoList(universities);
    }

    public UniversityDto getUniversityById(final UUID id) {
        return universityRepository.findById(id)
                                   .map(universityMapper::toDto)
                                   .orElseThrow(() -> new ResourceNotFoundException(
                                       UNIVERSITY_NOT_FOUND.getMessage(id.toString())));
    }

    @Transactional
    public UniversityDto updateUniversity(final UUID id,
                                          final UniversityDto universityDto) {
        return universityRepository.findById(id)
                                   .map(existingUniversity -> updateEntityAndConvertToDto(
                                       universityDto,
                                       existingUniversity))
                                   .orElseThrow(() -> new ResourceNotFoundException(
                                       UNIVERSITY_NOT_FOUND.getMessage(id.toString())));
    }

    @Transactional
    public void deleteUniversity(final UUID id) {
        final University university = universityRepository.findById(id)
                                                          .orElseThrow(() -> new ResourceNotFoundException(
                                                              UNIVERSITY_NOT_FOUND.getMessage(id.toString())));
        universityRepository.delete(university);
    }

    private UniversityDto updateEntityAndConvertToDto(final UniversityDto dto,
                                                      final University entity) {
        universityMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private UniversityDto saveEntityAndConvertToDto(final University entity) {
        final University savedEntity = universityRepository.save(entity);
        return universityMapper.toDto(savedEntity);
    }
}
