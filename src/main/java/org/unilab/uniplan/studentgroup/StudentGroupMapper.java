package org.unilab.uniplan.studentgroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupRequestDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupResponseDto;

@Mapper
public interface StudentGroupMapper {

    @Mapping(source = "id.studentId", target = "studentId")
    @Mapping(source = "id.courseGroupId", target = "courseGroupId")
    StudentGroupDto toDTO(StudentGroup studentGroup);

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    StudentGroup toEntity(StudentGroupDto dto);

    @Named("toStudentGroupId")
    default StudentGroupId toStudentGroupId(StudentGroupDto dto) {
        return new StudentGroupId(dto.studentId(), dto.courseGroupId());
    }

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    void updateEntityFromDTO(StudentGroupDto studentGroupDTO,
                             @MappingTarget StudentGroup studentGroup);

    StudentGroupDto toInnerDTO(StudentGroupRequestDto studentGroupRequestDTO);

    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "courseGroupId", target = "courseGroupId")
    StudentGroupResponseDto toResponseDTO(StudentGroupDto studentGroupDTO);

    List<StudentGroupResponseDto> toResponseDTOList(List<StudentGroupDto> studentGroups);
}
