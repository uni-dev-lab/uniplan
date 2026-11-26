package org.unilab.uniplan.building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BuildingRepository extends JpaRepository<Building, UUID> {

    @Query("""
        SELECT b
        FROM Building b
        LEFT JOIN FETCH b.university
        """)
    List<Building> findAllWithUniversity();

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Room r
        WHERE r.building.id = :buildingId
        """)
    boolean hasAssignedRooms(@Param("buildingId") UUID buildingId);
}
