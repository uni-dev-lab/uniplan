package org.unilab.uniplan.major;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

}
