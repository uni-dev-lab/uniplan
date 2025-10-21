package org.unilab.uniplan.student;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query("""
    select new org.unilab.uniplan.student.dto.StudentCourseMajorDto(
        s.id, s.firstName, s.lastName, s.facultyNumber,
        c.id, c.courseType, c.courseSubtype, c.courseYear,
        m.id, m.majorName
    )
    from Student s
    join s.course c
    join c.major m
    where (:firstName is null or lower(s.firstName) like lower(concat('%', :firstName, '%')))
      and (:lastName is null or lower(s.lastName) like lower(concat('%', :lastName, '%')))
      and (:facultyNumber is null or s.facultyNumber like concat('%', :facultyNumber, '%'))
      and (:majorName is null or lower(m.majorName) like lower(concat('%', :majorName, '%')))
    """)
    List<StudentCourseMajorDto> searchStudents(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("facultyNumber") String facultyNumber,
        @Param("majorName") String majorName
    );
}
