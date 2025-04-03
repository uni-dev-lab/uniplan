package org.unilab.uniplan.studentgroup;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupId implements Serializable {

    @Column(name = "STUDENT_ID")
    private UUID studentId;

    @Column(name = "GROUP_ID")
    private UUID courseGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentGroupId that = (StudentGroupId) o;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(courseGroupId, that.courseGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseGroupId);
    }
}
