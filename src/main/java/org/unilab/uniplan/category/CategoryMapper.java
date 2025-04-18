package org.unilab.uniplan.category;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;

@Mapper
public interface CategoryMapper {

    Category toEntity(final CategoryRequestDto categoryRequestDto);

    CategoryResponseDto toDto(final Category category);

    List<CategoryResponseDto> toDtoList(final List<Category> categories);

    @Mapping(target = "roomType", source = "categoryRequestDto.roomType")
    @Mapping(target = "capacity", source = "categoryRequestDto.capacity")
    void updateEntityFromDto(final CategoryRequestDto categoryRequestDto,
                             @MappingTarget final Category category);
}
