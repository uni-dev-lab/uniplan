package org.unilab.uniplan.studentgroup;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentGroupService {

    private static final String STUDENTGROUP_NOT_FOUND = "StudentGroup with ID {0} not found.";

    private final StudentGroupRepository studentGroupRepository;
    private final StudentGroupMapper studentGroupMapper;

    @Transactional
    public StudentGroupDTO createStudentGroup(final StudentGroupDTO studentGroupDTO) {
        StudentGroup studentGroup = studentGroupMapper.toEntity(studentGroupDTO);
        return studentGroupMapper.toDTO(studentGroupRepository.save(studentGroup));
    }

    @Transactional
    public Optional<StudentGroupDTO> updateStudentGroup(final UUID studentId,
                                                        final UUID courseGroupId,
                                                        final StudentGroupDTO studentGroupDTO) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);
        
        return studentGroupRepository.findById(id).map(
            existingStudentGroup -> {
                studentGroupMapper.updateEntityFromDTO(studentGroupDTO, existingStudentGroup);

                return studentGroupMapper.toDTO(studentGroupRepository.save(existingStudentGroup));
            });
    }

    @Transactional
    public void deleteStudentGroup(final UUID studentId, final UUID courseGroupId) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);
        final StudentGroup studentGroup = studentGroupRepository.findById(id)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                    MessageFormat.format(
                                                                        STUDENTGROUP_NOT_FOUND,
                                                                        id)));
        studentGroupRepository.delete(studentGroup);
    }

    public Optional<StudentGroupDTO> findStudentGroupById(final UUID studentId,
                                                          final UUID courseGroupId) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);

        return studentGroupRepository.findById(id)
                                     .map(studentGroupMapper::toDTO);
    }

    public List<StudentGroupDTO> findAll() {
        return studentGroupRepository.findAll()
                                     .stream().map(studentGroupMapper::toDTO).toList();
    }
}
