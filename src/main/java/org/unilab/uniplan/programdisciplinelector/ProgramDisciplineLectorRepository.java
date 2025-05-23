package org.unilab.uniplan.programdisciplinelector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramDisciplineLectorRepository extends
    JpaRepository<ProgramDisciplineLector, ProgramDisciplineLectorId> {

}
