package org.unilab.uniplan.university;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UniversityRepository extends JpaRepository<University, UUID> {
    
}