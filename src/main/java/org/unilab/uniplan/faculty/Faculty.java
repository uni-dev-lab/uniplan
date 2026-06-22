package org.unilab.uniplan.faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.common.model.SoftDeletableEntity;
import org.unilab.uniplan.university.University;

@Entity
@Table(name = "faculty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE faculty SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Faculty extends SoftDeletableEntity {

    @ManyToOne
    @JoinColumn(name = "uni_id", referencedColumnName = "id", nullable = false)
    private University university;
    @Column(name = "faculty_name", nullable = false, length = 200)
    private String facultyName;
    @Column(name = "location", length = 500)
    private String location;
}