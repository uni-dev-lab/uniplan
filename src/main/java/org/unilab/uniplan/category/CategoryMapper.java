package org.unilab.uniplan.category;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(final CategoryDto categoryDto);

    CategoryDto toDto(final Category category);

    List<CategoryDto> toDtoList(final List<Category> categories);

    @Mapping(target = "roomType", source = "categoryDto.roomType")
    @Mapping(target = "capacity", source = "categoryDto.capacity")
    void updateEntityFromDto(final CategoryDto categoryDto, @MappingTarget final Category category);
}
