package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;

    public void save(final RoomCategory roomCategory) {
        roomCategoryRepository.save(roomCategory);
    }

    public List<RoomCategory> getAll() {
        return roomCategoryRepository.findAll();
    }

    public Optional<RoomCategory> getById(final RoomCategoryId id) {
        return roomCategoryRepository.findById(id);
    }

    public void delete(RoomCategory roomCategory) {
        roomCategoryRepository.delete(roomCategory);
    }
}
