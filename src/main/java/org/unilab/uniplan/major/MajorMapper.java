package org.unilab.uniplan.major;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    Major toEntity (MajorDTO majorDTO);
    MajorDTO toDTO (Major major);
}