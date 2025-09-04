package org.unilab.uniplan.event.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unilab.uniplan.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
