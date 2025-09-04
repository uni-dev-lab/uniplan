package org.unilab.uniplan.event.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unilab.uniplan.event.RecurrenceInfo;

public interface RecurrenceInfoRepository extends JpaRepository<RecurrenceInfo, UUID> {

}
