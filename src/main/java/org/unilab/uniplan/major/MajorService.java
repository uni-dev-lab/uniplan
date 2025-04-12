package org.unilab.uniplan.major;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MajorService {
    private final MajorRepository majorRepository;
    private final FacultyService facultyService;
    private final MajorMapper majorMapper;

    @Autowired
    public MajorService(final MajorRepository majorRepository,
                        final FacultyService facultyService,
                        final MajorMapper majorMapper) {
        this.majorRepository = majorRepository;
        this.facultyService = facultyService;
        this.majorMapper = majorMapper;
    }

    @Transactional
    public MajorDTO createMajor(final MajorDTO majorDTO) {
        Faculty faculty = facultyService.getFaculty(majorDTO.facultyId())
                                        .orElseThrow(() ->new RuntimeException("Faculty with id" + majorDTO.facultyId() + "doesn't exists"));
        Major major = new Major(faculty, majorDTO.majorName());
        major.setCreatedAt();
        return majorMapper.toDTO(majorRepository.save(major));
    }

    public Optional<MajorDTO> findMajorById (final UUID id) {
        return majorRepository.findById(id)
                               .map(majorMapper::toDTO);
    }

    public Optional<Major> findById (final UUID id) {
        return majorRepository.findById(id);
    }
    public List<MajorDTO> findAll () {
        return majorRepository.findAll()
            .stream().map(majorMapper::toDTO).toList();
    }
    @Transactional
    public MajorDTO updateMajor (final UUID id, final MajorDTO majorDTO) {
        Major major = majorRepository.findById(id)
                                  .orElseThrow(()->new RuntimeException("Major with id " + id + " doesn't exists"));
       Faculty faculty = facultyService.getFaculty(majorDTO.facultyId())
                                       .orElseThrow(() ->new RuntimeException("Faculty with id" + majorDTO.facultyId() + "doesn't exists"));
        major.setFaculty(faculty);
        major.setMajorName(majorDTO.majorName());
        major.setUpdatedAt();
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
