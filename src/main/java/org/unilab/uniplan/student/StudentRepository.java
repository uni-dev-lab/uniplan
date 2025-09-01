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
    where (:firstName is null or s.firstName = :firstName)
      and (:lastName is null or s.lastName = :lastName)
      and (:facultyNumber is null or s.facultyNumber = :facultyNumber)
      and (:majorName is null or m.majorName = :majorName)
    """)
    List<StudentCourseMajorDto> searchStudents(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("facultyNumber") String facultyNumber,
        @Param("majorName") String majorName
    );
}
