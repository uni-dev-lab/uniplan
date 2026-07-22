package org.unilab.uniplan.exception;


import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

public class ProgramDisciplineNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineNotFoundException(final ProgramDisciplineId id) {
        super(String.format("Program discipline with ID %s not found.", id));
    }
}
