package org.unilab.uniplan.studentgroup;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;

@Service
@RequiredArgsConstructor
public class StudentGroupService {

    private static final String STUDENTGROUP_NOT_FOUND = "StudentGroup with ID {0} not found.";

    private final StudentGroupRepository studentGroupRepository;
    private final StudentGroupMapper studentGroupMapper;

    @Transactional
    public StudentGroupDto createStudentGroup(final StudentGroupDto studentGroupDTO) {
        StudentGroup studentGroup = studentGroupMapper.toEntity(studentGroupDTO);
        return saveEntityAndConvertToDto(studentGroup);
    }

    @Transactional
    public Optional<StudentGroupDto> updateStudentGroup(final UUID studentId,
                                                        final UUID courseGroupId,
                                                        final StudentGroupDto studentGroupDTO) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);

        return studentGroupRepository.findById(id).map(
            existingStudentGroup -> updateAndSaveEntityAndConvertToDto(studentGroupDTO, existingStudentGroup));
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

    public Optional<StudentGroupDto> findStudentGroupById(final UUID studentId,
                                                          final UUID courseGroupId) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);

        return studentGroupRepository.findById(id)
                                     .map(studentGroupMapper::toDto);
    }

    public List<StudentGroupDto> findAll() {
        return studentGroupRepository.findAll()
                                     .stream().map(studentGroupMapper::toDto).toList();
    }


    private StudentGroupDto updateAndSaveEntityAndConvertToDto(final StudentGroupDto dto,
                                                               final StudentGroup entity) {
        studentGroupMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private StudentGroupDto saveEntityAndConvertToDto(final StudentGroup entity) {
        final StudentGroup savedEntity = studentGroupRepository.save(entity);
        return studentGroupMapper.toDto(savedEntity);
    }
}
