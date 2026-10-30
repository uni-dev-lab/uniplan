package org.unilab.uniplan.program;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {

    boolean existsByCourse_Id(UUID courseId);

    boolean existsByCourse_IdAndIdNot(UUID courseId, UUID programId);
}
