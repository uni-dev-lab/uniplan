package org.unilab.uniplan.studentgroup;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
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
}
