package org.unilab.uniplan.studentgroup;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.coursegroup.CourseGroup;
import org.unilab.uniplan.coursegroup.CourseGroupService;
import org.unilab.uniplan.student.Student;
import org.unilab.uniplan.student.StudentService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentGroupService {
    private final StudentGroupRepository studentGroupRepository;
    private final StudentService studentService;
    private final CourseGroupService courseGroupService;
    private final StudentGroupMapper studentGroupMapper;

    @Autowired
    public StudentGroupService(final StudentGroupRepository studentGroupRepository,
                                final StudentService studentService,
                                final CourseGroupService courseGroupService,
                                final StudentGroupMapper studentGroupMapper) {
        this.studentGroupRepository = studentGroupRepository;
        this.studentService = studentService;
        this.courseGroupService = courseGroupService;
        this.studentGroupMapper = studentGroupMapper;
    }
    
    @Transactional
    public StudentGroupDTO createStudentGroup(final StudentGroupDTO studentGroupDTO) {
        Student student = studentService.findById(studentGroupDTO.studentId())
                                        .orElseThrow(() -> new RuntimeException("Student with id " + studentGroupDTO.studentId() + " doesn't exists"));
        CourseGroup courseGroup = courseGroupService.findById(studentGroupDTO.courseGroupId())
                                                    .orElseThrow(() -> new RuntimeException("CourseGroup with id " + studentGroupDTO.courseGroupId() + " doesn't exists"));
        StudentGroup studentGroup = new StudentGroup(new StudentGroupId(studentGroupDTO.studentId(),
                                                                        studentGroupDTO.courseGroupId()), student, courseGroup);
        studentGroup.setCreatedAt();
        return studentGroupMapper.toDTO(studentGroupRepository.save(studentGroup));
    }
    
    @Transactional
    public StudentGroupDTO updateStudentGroup(final UUID id, final StudentGroupDTO studentGroupDTO) {
        Student student = studentService.findById(studentGroupDTO.studentId())
                                        .orElseThrow(() -> new RuntimeException("Student with id " + studentGroupDTO.studentId() + " doesn't exists"));
        CourseGroup courseGroup = courseGroupService.findById(studentGroupDTO.courseGroupId())
                                                    .orElseThrow(() -> new RuntimeException("CourseGroup with id " + studentGroupDTO.courseGroupId() + " doesn't exists"));
        StudentGroup studentGroup = studentGroupRepository.findById(id)
                                                          .orElseThrow(() -> new RuntimeException("StudentGroup with id " + id + " doesn't exists"));
        studentGroup.setId(new StudentGroupId(studentGroupDTO.studentId(), studentGroupDTO.courseGroupId()));
        studentGroup.setStudent(student);
        studentGroup.setCourseGroup(courseGroup);
        studentGroup.setUpdatedAt();
        return studentGroupMapper.toDTO(studentGroupRepository.save(studentGroup));
    }
    
    @Transactional
    public boolean deleteStudentGroup(final UUID studentGroupId) {
        if (studentGroupRepository.existsById(studentGroupId)) {
            studentGroupRepository.deleteById(studentGroupId);
            return true;
        }
        return false;
    }
    
    public Optional<StudentGroupDTO> findStudentGroupById(final UUID id) {
        return studentGroupRepository.findById(id)
                                     .map(studentGroupMapper::toDTO);
    }
    
    public Optional<StudentGroup> findById(final UUID id) {
        return studentGroupRepository.findById(id);
    }
    
    public List<StudentGroupDTO> findAll() {
        return studentGroupRepository.findAll()
            .stream().map(studentGroupMapper::toDTO).toList();
    }
}
