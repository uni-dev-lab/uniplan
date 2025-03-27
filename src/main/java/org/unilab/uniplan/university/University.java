package org.unilab.uniplan.university;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;

@Entity
@Table(name = "UNIVERSITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class University extends BaseEntity {

    @Column(name = "UNI_NAME", nullable = false)
    private String uniName;
    @Column(name = "LOCATION", length = 500)
    private String location;
    @Column(name = "ESTABLISHED_YEAR")
    private short establishedYear;
    @Column(name = "ACCREDITATION", length = 200)
    private String accreditation;
    @Column(name = "WEBSITE", length = 2048)
    private String website;

}