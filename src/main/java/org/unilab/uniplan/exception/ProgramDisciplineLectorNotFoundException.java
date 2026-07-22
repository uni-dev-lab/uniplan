package org.unilab.uniplan.exception;


import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorId;

public class ProgramDisciplineLectorNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineLectorNotFoundException(final ProgramDisciplineLectorId id) {
        super(String.format("Program discipline lector with ID %s not found.", id));
    }
}
