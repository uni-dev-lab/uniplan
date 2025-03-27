package org.unilab.uniplan.programdisciplinelector;

import jakarta.persistence.Column;
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
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.lector.Lector;
import org.unilab.uniplan.program.Program;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LECTOR_PROGRAM")
public class ProgramDisciplineLector extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "LECTOR_TYPE", nullable = false, length = 100)
    private LectorType lectorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTOR_ID", referencedColumnName = "ID", nullable = false)
    private Lector lector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAM_ID", referencedColumnName = "ID", nullable = false)
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID", nullable = false)
    private Discipline discipline;
}
