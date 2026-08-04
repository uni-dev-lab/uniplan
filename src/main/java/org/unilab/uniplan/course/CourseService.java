package org.unilab.uniplan.course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class CourseService implements BaseService<Course> {

    private final CourseRepository courseRepository;

    @Override
    public void save(final Course entity) {
         courseRepository.save(entity);
    }

    @Override
    public Optional<Course> getById(final UUID id) {
        return courseRepository.findById(id);
    }

    public List<Course> findAllByMajorId(final UUID majorId) {
        return courseRepository.findAllByMajorId(majorId);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public void delete(final Course entity) {
        courseRepository.delete(entity);
    }
}
