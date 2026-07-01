package org.unilab.uniplan.roomcategory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;

@ExtendWith(MockitoExtension.class)
class RoomCategoryServiceTest {

    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @InjectMocks
    private RoomCategoryService roomCategoryService;

    private RoomCategoryId id;
    private RoomCategory entity;

    @BeforeEach
    void setUp() {
        id = new RoomCategoryId(UUID.randomUUID(), UUID.randomUUID());
        entity = new RoomCategory();
    }

    @Test
    void testSaveShouldSaveRoomCategory() {
        roomCategoryService.save(entity);

        verify(roomCategoryRepository).save(entity);
    }

    @Test
    void testGetAllRoomCategoriesShouldReturnListOfRoomCategory() {
        final List<RoomCategory> entities = List.of(entity);

        when(roomCategoryRepository.findAll()).thenReturn(entities);

        final List<RoomCategory> result = roomCategoryService.getAll();

        assertEquals(entities, result);
        verify(roomCategoryRepository).findAll();
    }

    @Test
    void testGetByIdShouldReturnRoomCategoryOptional() {
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.of(entity));

        final Optional<RoomCategory> result = roomCategoryService.getById(id);
        assertEquals((Optional.of(entity)), result);
    }

    @Test
    void testGetIdShouldReturnEmptyOptionalIfNotFound() {
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<RoomCategory> result = roomCategoryService.getById(id);

        assertEquals(Optional.empty(), result);
        verify(roomCategoryRepository).findById(id);
    }

    @Test
    void testDeleteShouldDeleteIfFound() {
       roomCategoryService.delete(entity);

        verify(roomCategoryRepository).delete(entity);
    }
}
