package org.unilab.uniplan.student;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    
    @Mapping(source = "courseId", target = "course.id")
    Student toEntity (StudentDTO studentDTO);

    @Mapping(source = "course.id", target = "courseId")
    StudentDTO toDTO (Student student);

    
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDTO(StudentDTO studentDTO, @MappingTarget Student student);

    @Mapping(target = "id", ignore = true)
    StudentDTO toInternalDTO (StudentRequestDTO student);
    @Mapping(source = "courseId", target = "courseId")
    StudentResponseDTO toResponseDTO (StudentDTO studentDTO);
    List<StudentResponseDTO> toResponseDTOList (List<StudentDTO> students);
    
}