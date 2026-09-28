package org.unilab.uniplan.exception;


import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorId;

public class ProgramDisciplineLectorNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineLectorNotFoundException(final ProgramDisciplineLectorId id) {
        super("program_discipline_lector_not_found", id);
    }
}
