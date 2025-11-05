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
    StudentGroupDto toDto(final StudentGroup studentGroup);

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "courseGroup.id", source = "courseGroupId")
    StudentGroup toEntity(final StudentGroupDto dto);

    @Named("toStudentGroupId")
    default StudentGroupId toStudentGroupId(final StudentGroupDto dto) {
        return new StudentGroupId(dto.studentId(), dto.courseGroupId());
    }

    @Mapping(target = "id", source = ".", qualifiedByName = "toStudentGroupId")
    @Mapping(target = "student.id", ignore = true)
    @Mapping(target = "courseGroup.id", ignore = true)
    void updateEntityFromDto(final StudentGroupDto studentGroupDto,
                             @MappingTarget final StudentGroup studentGroup);

    StudentGroupDto toInnerDto(final StudentGroupRequestDto studentGroupRequestDto);

    StudentGroupResponseDto toResponseDto(final StudentGroupDto studentGroupDto);

    List<StudentGroupResponseDto> toResponseDtoList(final List<StudentGroupDto> studentGroups);
}
