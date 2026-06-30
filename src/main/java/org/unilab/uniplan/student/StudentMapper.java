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
    @Mapping(target = "course", ignore = true)
    void updateEntity(StudentRequestDto requestDto, @MappingTarget Student student);

    @Mapping(target = "name", expression = "java(toFullName(student.getFirstName(), student.getLastName()))")
    @Mapping(source = "course.major.id", target = "majorId")
    @Mapping(source = "course.major.majorName", target = "majorName")
    @Mapping(source = "course.courseType", target="courseType")
    @Mapping(source = "course.courseSubtype", target = "courseSubtype")
    @Mapping(source = "course.courseYear", target = "courseYear")
    StudentResponseDto toResponseDto(Student student);

    List<StudentResponseDto> toResponseDtoList(List<Student> students);

    default String toFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}