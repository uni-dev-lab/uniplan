package org.unilab.uniplan.coursegroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CourseGroupRepository extends JpaRepository<CourseGroup, UUID> {

}
