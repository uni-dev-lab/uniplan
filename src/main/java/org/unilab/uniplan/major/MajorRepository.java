package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unilab.uniplan.major.dto.MajorCourseDto;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {
    @Query("""
            select new org.unilab.uniplan.major.dto.MajorCourseDto(
                m.id,
                c.id,
                m.faculty.id,
                m.majorName,
                c.courseType,
                c.courseSubtype
                ) from Major m join m.courses c
    """)
    List<MajorCourseDto> findAllMajorsWithCourse();

    @Query("""
            select new org.unilab.uniplan.major.dto.MajorCourseDto(
                m.id,
                c.id,
                m.faculty.id,
                m.majorName,
                c.courseType,
                c.courseSubtype
                )
                from Major m
                join m.courses c
                where m.id = :majorId
    """)
    List<MajorCourseDto> findMajorWithCourse(@Param("majorId") final UUID majorId);
           
    public List<Major> findAllByFacultyId(UUID facultyId);
}
