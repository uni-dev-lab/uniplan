package org.unilab.uniplan.studentgroup;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.StudentGroupNotFoundException;
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;

@Service
@RequiredArgsConstructor
public class StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;
    private final StudentGroupMapper studentGroupMapper;

    @Transactional
    public StudentGroupDto createStudentGroup(final StudentGroupDto studentGroupDTO) {
        StudentGroup studentGroup = studentGroupMapper.toEntity(studentGroupDTO);
        return saveEntityAndConvertToDto(studentGroup);
    }

    @Transactional
    public StudentGroupDto updateStudentGroup(final UUID studentId,
                                                        final UUID courseGroupId,
                                                        final StudentGroupDto studentGroupDTO) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);

        return studentGroupRepository.findById(id).map(
                                         existingStudentGroup -> updateEntityAndConvertToDto(studentGroupDTO,
                                                                                             existingStudentGroup))
                                     .orElseThrow(() -> new StudentGroupNotFoundException(id));
    }

    @Transactional
    public void deleteStudentGroup(final UUID studentId, final UUID courseGroupId) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);
        final StudentGroup studentGroup = studentGroupRepository.findById(id)
                                                                .orElseThrow(() -> new StudentGroupNotFoundException(id));

        studentGroupRepository.delete(studentGroup);
    }

    public StudentGroupDto findStudentGroupById(final UUID studentId,
                                                          final UUID courseGroupId) {
        final StudentGroupId id = new StudentGroupId(studentId, courseGroupId);

        return studentGroupRepository.findById(id)
                                     .map(studentGroupMapper::toDto)
                                     .orElseThrow(() -> new StudentGroupNotFoundException(id));

    }

    public List<StudentGroupDto> findAll() {
        return studentGroupRepository.findAll()
                                     .stream().map(studentGroupMapper::toDto).toList();
    }


    private StudentGroupDto updateEntityAndConvertToDto(final StudentGroupDto dto,
                                                        final StudentGroup entity) {
        studentGroupMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private StudentGroupDto saveEntityAndConvertToDto(final StudentGroup entity) {
        final StudentGroup savedEntity = studentGroupRepository.save(entity);
        return studentGroupMapper.toDto(savedEntity);
    }
}
