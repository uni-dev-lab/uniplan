package org.unilab.uniplan.major;

import org.unilab.uniplan.common.model.BaseService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorService implements BaseService<Major> {

    private final MajorRepository majorRepository;

    @Override
    public void save(final Major major) {
        majorRepository.save(major);
    }

    @Override
    public Optional<Major> getById(final UUID id) {
        return majorRepository.findById(id);
    }

    @Override
    public List<Major> getAll() {
        return majorRepository.findAll();
    }

    @Override
    public void delete(final Major major) {
        majorRepository.delete(major);
    }

    public List<Major> findAllMajorByFacultyId(final UUID facultyId) {
        return majorRepository.findAllMajorByFacultyId(facultyId);
    }

    public List<Major> findAllMajorWithCoursesByFacultyId(final UUID facultyId) {
        return majorRepository.findAllMajorWithCoursesByFacultyId(facultyId);
    }
}
