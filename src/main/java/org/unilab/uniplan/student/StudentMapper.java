package org.unilab.uniplan.student;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity (StudentDTO studentDTO);
    StudentDTO toDTO (Student student);
}
