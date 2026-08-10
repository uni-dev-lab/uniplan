package org.unilab.uniplan.exception;


import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

public class ProgramDisciplineNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineNotFoundException(final ProgramDisciplineId id) {
        super("program_discipline_not_found", id);
    }
}
