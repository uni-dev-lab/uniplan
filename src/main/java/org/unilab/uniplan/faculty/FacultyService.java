package org.unilab.uniplan.faculty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final UniversityService universityService;
    private final FacultyMapper facultyMapper;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository,
                          UniversityService universityService,
                          FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.universityService = universityService;
        this.facultyMapper = facultyMapper;
    }

    @Transactional
    public FacultyDto createFaculty(FacultyDto facultyDto) {
        University university = universityService.getUniversity(facultyDto.universityId())
                                                 .orElseThrow(() -> new IllegalArgumentException(
                                                     "University not found"));
        Faculty faculty = facultyMapper.toEntity(facultyDto);
        faculty.setFacultyName(facultyDto.facultyName());
        faculty.setLocation(facultyDto.location());
        faculty.setUniversity(university);
        faculty = facultyRepository.save(faculty);
        return facultyMapper.toDto(faculty);
    }

    public List<FacultyDto> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream().map(facultyMapper::toDto).toList();
    }

    public Optional<FacultyDto> getFacultyById(UUID id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        return faculty.map(facultyMapper::toDto);
    }

    public Optional<Faculty> getFaculty(UUID id) {
        return facultyRepository.findById(id);
    }

    @Transactional
    public Optional<FacultyDto> updateFaculty(UUID id, FacultyDto facultyDto) {
        Optional<Faculty> existingFaculty = facultyRepository.findById(id);
        if (existingFaculty.isPresent()) {
            University university = universityService.getUniversity(facultyDto.universityId())
                                                     .orElseThrow(() -> new IllegalArgumentException(
                                                         "University not found"));
            Faculty faculty = existingFaculty.get();
            faculty.setFacultyName(facultyDto.facultyName());
            faculty.setLocation(facultyDto.location());
            faculty.setUniversity(university);
            faculty = facultyRepository.save(faculty);
            return Optional.of(facultyMapper.toDto(faculty));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteFaculty(UUID id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
