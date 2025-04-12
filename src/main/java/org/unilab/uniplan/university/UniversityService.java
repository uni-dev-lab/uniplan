package org.unilab.uniplan.university;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;

    @Transactional
    public Optional<UniversityDto> createUniversity(final UniversityDto universityDto) {
        final University university = universityMapper.toEntity(universityDto);
        final University savedUniversity = universityRepository.save(university);
        return Optional.of(universityMapper.toDto(savedUniversity));
    }

    public List<UniversityDto> getAllUniversities() {
        final List<University> universities = universityRepository.findAll();
        return universityMapper.toDtoList(universities);
    }

    public Optional<UniversityDto> getUniversityById(final UUID id) {
        final Optional<University> university = universityRepository.findById(id);
        return university.map(universityMapper::toDto);
    }

    @Transactional
    public Optional<UniversityDto> updateUniversity(final UUID id,
                                                    final UniversityDto universityDto) {
        final Optional<University> existingUniversity = universityRepository.findById(id);

        if (existingUniversity.isPresent()) {
            final University university = existingUniversity.get();
            universityMapper.updateEntityFromDto(universityDto, university);
            final University savedUniversity = universityRepository.save(university);
            return Optional.of(universityMapper.toDto(savedUniversity));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<UniversityDto> deleteUniversity(final UUID id) {
        final Optional<University> universityOpt = universityRepository.findById(id);

        if (universityOpt.isPresent()) {
            final University university = universityOpt.get();
            universityRepository.delete(university);
            return Optional.of(universityMapper.toDto(university));
        }

        return Optional.empty();
    }
}
