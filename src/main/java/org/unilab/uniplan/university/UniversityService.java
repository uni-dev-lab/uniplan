package org.unilab.uniplan.university;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;

    @Autowired
    public UniversityService(UniversityRepository universityRepository,
                             UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    @Transactional
    public UniversityDto createUniversity(UniversityDto universityDto) {
        University university = universityMapper.toEntity(universityDto);
        university.setUniName(universityDto.uniName());
        university.setLocation(universityDto.location());
        university.setEstablishedYear(universityDto.establishedYear());
        university.setAccreditation(universityDto.accreditation());
        university.setWebsite(universityDto.website());
        university = universityRepository.save(university);
        return universityMapper.toDto(university);
    }

    public List<UniversityDto> getAllUniversities() {
        List<University> universities = universityRepository.findAll();
        return universities.stream().map(universityMapper::toDto).toList();
    }

    public Optional<UniversityDto> getUniversityById(UUID id) {
        Optional<University> university = universityRepository.findById(id);
        return university.map(universityMapper::toDto);
    }

    public Optional<University> getUniversity(UUID id) {
        return universityRepository.findById(id);
    }

    @Transactional
    public Optional<UniversityDto> updateUniversity(UUID id, UniversityDto universityDto) {
        Optional<University> existingUniversity = universityRepository.findById(id);
        if (existingUniversity.isPresent()) {
            University university = existingUniversity.get();
            university.setUniName(universityDto.uniName());
            university.setLocation(universityDto.location());
            university.setEstablishedYear(universityDto.establishedYear());
            university.setAccreditation(universityDto.accreditation());
            university.setWebsite(universityDto.website());
            university = universityRepository.save(university);
            return Optional.of(universityMapper.toDto(university));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteUniversity(UUID id) {
        Optional<University> university = universityRepository.findById(id);
        if (university.isPresent()) {
            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
