package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

    @Query(
        "SELECT new org.unilab.uniplan.major.MajorCourseModel( MAJ.id, COU.id, MAJ.faculty.id, MAJ.majorName, COU.courseType, COU.courseSubtype) "
        + " FROM Major MAJ JOIN Course COU ON MAJ.id = COU.major.id")
    List<MajorCourseModel> findMajorWithCourse();

}
