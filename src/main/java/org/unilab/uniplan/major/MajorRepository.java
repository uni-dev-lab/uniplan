package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

    public List<Major> findAllByFacultyId(UUID facultyId);
}
