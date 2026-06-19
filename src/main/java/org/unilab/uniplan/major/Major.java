package org.unilab.uniplan.major;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.unilab.uniplan.common.model.SoftDeletableEntity;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.faculty.Faculty;

@Entity
@Table(name = "major")
@SQLDelete(sql = "UPDATE major SET deleted_at = NOW() WHERE id = ?")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major extends SoftDeletableEntity {
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "major_name", nullable = false, length = 200)
    private String majorName;

    //Read-only list of courses
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "major")
    private List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }
}