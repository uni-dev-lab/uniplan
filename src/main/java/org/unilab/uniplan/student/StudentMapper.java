package org.unilab.uniplan.student;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
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

    @Mapping(target = "name",         expression = "java(toFullName(dto.firstName(), dto.lastName()))")
    @Mapping(target = "facultyNumber", source = "facultyNumber")
    @Mapping(target = "majorName",     source = "majorName")
    @Mapping(target = "courseType",    source = "courseType")
    @Mapping(target = "courseSubtype", source = "courseSubType")
    @Mapping(target = "courseYear",    source = "courseYear")
    StudentResponseDto toResponseDto(StudentCourseMajorDto dto);

    List<StudentResponseDto> toResponseDtoList(List<StudentCourseMajorDto> students);

    default String toFullName(String firstName, String lastName){
        return firstName +" "+lastName;
    }

}