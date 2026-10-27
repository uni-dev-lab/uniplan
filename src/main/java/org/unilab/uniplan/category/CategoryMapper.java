package org.unilab.uniplan.category;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;

@Mapper
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(final CategoryRequestDto categoryRequestDto);

    CategoryResponseDto toResponseDto(final Category category);

    List<CategoryResponseDto> toResponseDtoList(final List<Category> categories);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final CategoryRequestDto categoryRequestDto,
                             @MappingTarget final Category category);
}
