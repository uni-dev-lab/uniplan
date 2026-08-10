package org.unilab.uniplan.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.common.JoinType;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaEntityJoin;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.data.repository.query.Param;
import org.unilab.uniplan.category.Category;
import org.unilab.uniplan.room.dto.RoomResponseDto;
import org.unilab.uniplan.roomcategory.RoomCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomRoomRepositoryImpl implements CustomRoomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoomResponseDto> findAllRoomResponses() {
        HibernateCriteriaBuilder cb = (HibernateCriteriaBuilder) entityManager.getCriteriaBuilder();

        JpaCriteriaQuery<RoomResponseDto> query = cb.createQuery(RoomResponseDto.class);

        JpaRoot<Room> room = query.from(Room.class);

        JpaEntityJoin<Room, RoomCategory> roomCategory =
            room.join(RoomCategory.class, JoinType.LEFT);
        roomCategory.on(cb.equal(roomCategory.get("room"), room));

        JpaEntityJoin<RoomCategory, Category> category =
            roomCategory.join(Category.class, JoinType.LEFT);
        category.on(cb.equal(category, roomCategory.get("category")));

        query.select(cb.construct(
            RoomResponseDto.class,
            room.get("id"),
            room.get("faculty").get("id"),
            room.get("roomNumber"),
            room.get("availableSeats"),
            category.get("id"),
            room.get("building").get("id")
        ));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<RoomResponseDto> findRoomResponseById(UUID id) {
        HibernateCriteriaBuilder cb = (HibernateCriteriaBuilder) entityManager.getCriteriaBuilder();

        JpaCriteriaQuery<RoomResponseDto> query = cb.createQuery(RoomResponseDto.class);

        JpaRoot<Room> room = query.from(Room.class);
        JpaEntityJoin<Room, RoomCategory> roomCategory =
            room.join(RoomCategory.class, JoinType.LEFT);
        roomCategory.on(cb.equal(roomCategory.get("room"), room));

        JpaEntityJoin<RoomCategory, Category> category =
            roomCategory.join(Category.class, JoinType.LEFT);
        category.on(cb.equal(category, roomCategory.get("category")));

        query.select(cb.construct(
            RoomResponseDto.class,
            room.get("id"),
            room.get("faculty").get("id"),
            room.get("roomNumber"),
            room.get("availableSeats"),
            category.get("id"),
            room.get("building").get("id")
        )).where(cb.equal(room.get("id"), id));

        return entityManager.createQuery(query)
                            .getResultStream()
                            .findFirst();
    }
}