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
    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentRequestDto requestDto);

    @Mapping(source = "course.id", target = "courseId")
    StudentDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courseId", target = "course.id")
    void updateEntity(StudentRequestDto requestDto, @MappingTarget Student student);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(target = "name", expression = "java(toFullName(dto.firstName(), dto.lastName()))")
    StudentResponseDto toResponseDto(Student student);

    List<StudentResponseDto> toResponseDtoList(List<Student> students);

}