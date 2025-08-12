package org.unilab.uniplan.discipline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;

@Entity
@Table(name = "disciplines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discipline extends BaseEntity {

    @Column(name = "discipline_name", nullable = false, length = 100)
    private String name;

    @Column(name = "main_lector", nullable = false, length = 200)
    private String mainLector;

    @OneToMany(mappedBy = "discipline")
    private List<ProgramDiscipline> programDisciplines = new ArrayList<>();
}
