package org.unilab.uniplan.student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class StudentService implements BaseService<Student> {

    private final StudentRepository studentRepository;

    @Override
    public void save(final Student entity) {
        studentRepository.save(entity);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAllWithCourseAndMajor();
    }

    @Override
    public Optional<Student> getById(final UUID id) {
        return studentRepository.findByIdWithCourseAndMajor(id);
    }

    @Override
    public void delete(final Student entity) {
        studentRepository.delete(entity);
    }
}
