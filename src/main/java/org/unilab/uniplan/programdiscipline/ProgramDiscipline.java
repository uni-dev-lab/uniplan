package org.unilab.uniplan.programdiscipline;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.AuditableEntity;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.program.Program;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DISCIPLINE_PROGRAM")
public class ProgramDiscipline extends AuditableEntity {

    @EmbeddedId
    private ProgramDisciplineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID", nullable = false)
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAM_ID", referencedColumnName = "ID", nullable = false)
    private Program program;

    @Column(name = "HOURS_LECTURE", nullable = false)
    private short hoursLecture;

    @Column(name = "HOURS_EXERCISES", nullable = false)
    private short hoursExercises;

    @Column(name = "SEMESTER_COUNT", nullable = false)
    @Min(value = 1, message = "Semester count must be between 1 and 12")
    @Max(value = 12, message = "Semester count must be between 1 and 12")
    private byte semesterCount;
}
