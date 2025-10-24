package org.unilab.uniplan.major;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.faculty.Faculty;

@Entity
@Table(name = "major")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "major_name", nullable = false, length = 200)
    private String majorName;

    @OneToMany(mappedBy = "major", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<Course> courses = new ArrayList<>();

    public void addCourse(final Course course){
        this.getCourses().add(course);
        course.setMajor(this);
    }

    public void removeCourse(final Course course){
        this.getCourses().remove(course);
        course.setMajor(null);
    }
}
