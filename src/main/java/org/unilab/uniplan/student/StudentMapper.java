package org.unilab.uniplan.student;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@Mapper
public interface StudentMapper {

    @Mapping(source = "courseId", target = "course.id")
    Student toEntity(StudentDto studentDto);

    @Mapping(source = "course.id", target = "courseId")
    StudentDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDto(StudentDto studentDto, @MappingTarget Student student);

    @Mapping(target = "id", ignore = true)
    StudentDto toInternalDto(StudentRequestDto student);

    @Mapping(target = "majorName", source = "majorDto.majorName")
    @Mapping(target = "courseType", source = "courseDto.courseType")
    @Mapping(target = "courseSubtype", source = "courseDto.courseSubtype")
    @Mapping(target = "courseYear", source = "courseDto.courseYear")
    @Mapping(target = "id", source = "studentDto.id")
    @Mapping(target = "firstName", source = "studentDto.firstName")
    @Mapping(target = "lastName", source = "studentDto.lastName")
    @Mapping(target = "facultyNumber", source = "studentDto.facultyNumber")
    StudentResponseDto toResponseDto(StudentDto studentDto, CourseDto courseDto, MajorDto majorDto);

    List<StudentResponseDto> toResponseDtoList(List<StudentDto> students);

}