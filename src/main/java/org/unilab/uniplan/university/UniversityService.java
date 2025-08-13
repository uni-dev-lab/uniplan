package org.unilab.uniplan.university;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLector;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import org.unilab.uniplan.university.dto.UniversityDto;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private static final String UNIVERSITY_NOT_FOUND = "University with ID {0} not found.";

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

    public Optional<UniversityDto> getUniversityById(final UUID id) {
        return universityRepository.findById(id)
                                   .map(universityMapper::toDto);
    }

    @Transactional
    public Optional<UniversityDto> updateUniversity(final UUID id,
                                                    final UniversityDto universityDto) {
        return universityRepository.findById(id)
                                   .map(existingUniversity -> updateAndSaveEntityAndConvertToDto(
                                       universityDto,
                                       existingUniversity));
    }

    @Transactional
    public void deleteUniversity(final UUID id) {
        final University university = universityRepository.findById(id)
                                                          .orElseThrow(() -> new RuntimeException(
                                                              MessageFormat.format(
                                                                  UNIVERSITY_NOT_FOUND,
                                                                  id)));
        universityRepository.delete(university);
    }

    private UniversityDto updateAndSaveEntityAndConvertToDto(final UniversityDto dto,
                                                             final University entity) {
        universityMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private UniversityDto saveEntityAndConvertToDto(final University entity) {
        final University savedEntity = universityRepository.save(entity);
        return universityMapper.toDto(savedEntity);
    }
}
