package org.unilab.uniplan.room;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    @Query("""
        SELECT new org.unilab.uniplan.room.dto.RoomResponseDto(
            r.id,
            r.faculty.id,
            r.roomNumber,
            c.id
        )
        FROM Room r
        JOIN RoomCategory rc ON rc.room = r
        JOIN rc.category c
        """)
    List<RoomResponseDto> findAllRoomResponses();

    @Query("""
        SELECT new org.unilab.uniplan.room.dto.RoomResponseDto(
            r.id,
            r.faculty.id,
            r.roomNumber,
            c.id
        )
        FROM RoomCategory rc
        JOIN rc.room r
        JOIN rc.category c
        WHERE r.id = :id
        """)
    Optional<RoomResponseDto> findRoomResponseById(@Param("id") UUID id);

}