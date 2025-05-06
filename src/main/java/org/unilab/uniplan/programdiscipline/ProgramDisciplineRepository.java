package org.unilab.uniplan.programdiscipline;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramDisciplineRepository extends JpaRepository<ProgramDiscipline, UUID> {
    @Query("SELECT * FROM DISCIPLINE_PROGRAM pg WHERE pg.id = ?1")
     ProgramDiscipline findByProgramId(ProgramDisciplineId id);

}
