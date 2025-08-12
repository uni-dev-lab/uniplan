package org.unilab.uniplan.program;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;

@Entity
@Table(name = "programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Program extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "program")
    private List<ProgramDiscipline> programDisciplines = new ArrayList<>();
}
