package org.unilab.uniplan.programdisciplinelector;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDisciplineLectorId implements Serializable {

    @Column(name = "LECTOR_ID")
    private UUID lectorId;

    @Column(name = "PROGRAM_ID")
    private UUID programId;

    @Column(name = "DISCIPLINE_ID")
    private UUID disciplineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramDisciplineLectorId that = (ProgramDisciplineLectorId) o;
        return Objects.equals(lectorId, that.lectorId) &&
               Objects.equals(programId, that.programId) &&
               Objects.equals(disciplineId, that.disciplineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectorId, programId, disciplineId);
    }
}

