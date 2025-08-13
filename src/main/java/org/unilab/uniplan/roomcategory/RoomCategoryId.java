package org.unilab.uniplan.roomcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategoryId implements Serializable {

    @Column(name = "ROOM_ID")
    private UUID roomId;
    @Column(name = "CATEGORY_ID")
    private UUID categoryId;

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        RoomCategoryId that = (RoomCategoryId) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, categoryId);
    }
}
