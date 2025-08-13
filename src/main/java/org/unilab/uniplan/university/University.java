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
@Table(name = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class University extends BaseEntity {

    @Column(name = "uni_name", nullable = false)
    private String uniName;
    @Column(name = "location", length = 500)
    private String location;
    @Column(name = "established_year")
    private short establishedYear;
    @Column(name = "accreditation", length = 200)
    private String accreditation;
    @Column(name = "website", length = 2048)
    private String website;

}