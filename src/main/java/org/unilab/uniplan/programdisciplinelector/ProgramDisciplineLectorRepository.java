package org.unilab.uniplan.programdisciplinelector;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;

@Repository
public interface ProgramDisciplineLectorRepository extends
    JpaRepository<ProgramDisciplineLector, UUID> {

    @Query("SELECT * FROM LECTOR_PROGRAM lp WHERE lg.id = ?1")
    Optional<ProgramDisciplineLector> findByProgramDisciplineLectorId(ProgramDisciplineLectorId id);
}
