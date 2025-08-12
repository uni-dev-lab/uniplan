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

@Entity
@Table(name = "program_discipline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDiscipline extends AuditableEntity {

    @EmbeddedId
    private ProgramDisciplineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Program program;

    @Column(name = "hours_lecture", nullable = false)
    private short hoursLecture;

    @Column(name = "hours_exercises", nullable = false)
    private short hoursExercises;

    @Column(name = "semester_count", nullable = false)
    @Min(value = 1, message = "Semester count must be between 1 and 12")
    @Max(value = 12, message = "Semester count must be between 1 and 12")
    private byte semesterCount;
}
