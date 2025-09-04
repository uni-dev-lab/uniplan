package org.unilab.uniplan.course;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    public List<Course> findAllByMajorId(UUID majorId);
}
