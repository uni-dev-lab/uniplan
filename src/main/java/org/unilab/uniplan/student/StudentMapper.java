package org.unilab.uniplan.student;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@Mapper
public interface StudentMapper {

    @Mapping(source = "courseId", target = "course.id")
    Student toEntity(final StudentDto studentDto);

    @Mapping(source = "course.id", target = "courseId")
    StudentDto toDto(final Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDto(final StudentDto studentDto, @MappingTarget final Student student);

    @Mapping(target = "id", ignore = true)
    StudentDto toInternalDto(final StudentRequestDto student);

    StudentResponseDto toResponseDto(final StudentDto studentDto);

    List<StudentResponseDto> toResponseDtoList(final List<StudentDto> students);

}