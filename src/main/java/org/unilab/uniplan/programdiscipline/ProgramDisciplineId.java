package org.unilab.uniplan.programdiscipline;

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
public class ProgramDisciplineId implements Serializable {

    @Column(name = "DISCIPLINE_ID")
    private UUID disciplineId;

    @Column(name = "PROGRAM_ID")
    private UUID programId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramDisciplineId that = (ProgramDisciplineId) o;
        return Objects.equals(disciplineId, that.disciplineId) &&
               Objects.equals(programId, that.programId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disciplineId, programId);
    }

    @Override
    public String toString() {
        return disciplineId.toString() + ", " + programId.toString();
    }
}

