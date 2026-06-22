package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unilab.uniplan.faculty.Faculty;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

    List<Major> findAllByFacultyId(UUID facultyId);

    List<Major> findAllByFaculty(Faculty faculty);
}
