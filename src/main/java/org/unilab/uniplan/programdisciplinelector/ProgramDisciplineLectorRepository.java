package org.unilab.uniplan.programdisciplinelector;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramDisciplineLectorRepository extends
    JpaRepository<ProgramDisciplineLector, UUID> {

}
