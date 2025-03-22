package org.unilab.uniplan.programdiscipline;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramDisciplineRepository extends JpaRepository<ProgramDiscipline, UUID> {

}
