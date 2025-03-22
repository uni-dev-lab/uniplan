package org.unilab.uniplan.roomcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, UUID> {

}