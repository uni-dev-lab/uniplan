package org.unilab.uniplan.discipline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DISCIPLINE")
public class Discipline extends BaseEntity {

    @Column(name = "DISCIPLINE_NAME", nullable = false)
    private String name;

    @Column(name = "MAIN_LECTOR", nullable = false)
    private String mainLector;

    @OneToMany(mappedBy = "discipline")
    private List<ProgramDiscipline> programDisciplines;
}
