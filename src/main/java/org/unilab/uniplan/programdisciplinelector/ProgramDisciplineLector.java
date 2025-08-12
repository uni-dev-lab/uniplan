package org.unilab.uniplan.programdisciplinelector;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.AuditableEntity;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.lector.Lector;
import org.unilab.uniplan.program.Program;

@Entity
@Table(name = "lector_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDisciplineLector extends AuditableEntity {

    @EmbeddedId
    private ProgramDisciplineLectorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lector_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Lector lector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Discipline discipline;

    @Enumerated(EnumType.STRING)
    @Column(name = "lector_type", nullable = false, length = 100)
    private LectorType lectorType;
}
