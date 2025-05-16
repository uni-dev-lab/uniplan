package org.unilab.uniplan.studentgroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface StudentGroupMapper {

    @Mapping(source = "id.studentId", target = "studentId")
    @Mapping(source = "id.courseGroupId", target = "courseGroupId")
    @Mapping(target = "id", ignore = true)
    StudentGroupDTO toDTO(StudentGroup studentGroup);

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    StudentGroup toEntity(StudentGroupDTO dto);

    @Named("toStudentGroupId")
    default StudentGroupId toStudentGroupId(StudentGroupDTO dto) {
        return new StudentGroupId(dto.studentId(), dto.courseGroupId());
    }

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    void updateEntityFromDTO(StudentGroupDTO studentGroupDTO,
                             @MappingTarget StudentGroup studentGroup);

    @Mapping(target = "id", ignore = true)
    StudentGroupDTO toInnerDTO(StudentGroupRequestDTO studentGroupRequestDTO);

    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "courseGroupId", target = "courseGroupId")
    StudentGroupResponseDTO toResponseDTO(StudentGroupDTO studentGroupDTO);

    List<StudentGroupResponseDTO> toResponseDTOList(List<StudentGroupDTO> studentGroups);
}
