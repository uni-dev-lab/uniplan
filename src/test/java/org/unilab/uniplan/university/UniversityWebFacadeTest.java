package org.unilab.uniplan.university;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
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
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

@ExtendWith(MockitoExtension.class)
class UniversityWebFacadeTest {
    @Mock
    private UniversityMapper universityMapper;

    @Mock
    private UniversityService universityService;

    @InjectMocks
    private UniversityWebFacade universityWebFacade;

    private UUID id;
    private University entity;
    private UniversityRequestDto requestDto;
    private UniversityResponseDto responseDto;
/*
    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new University();
        requestDto = new UniversityRequestDto("University of Sofia", "Sofia", (short) 1888,
                                              "Excellent", "www.uni-sofia.bg");
        responseDto = new UniversityResponseDto(id, "University of Sofia", "Sofia", (short) 1888,
                                                "Excellent", "www.uni-sofia.bg");
    }

    @Test
    void createUniversity_shouldReturnResponseDto_whenRequestIsValid() {
        when(universityMapper.toEntity(requestDto)).thenReturn(entity);
        when(universityMapper.toResponseDto(entity)).thenReturn(responseDto);

        final var result = universityWebFacade.createUniversity(requestDto);

        assertThat(result).isEqualTo(responseDto);

        final var inOrder = inOrder(universityMapper, universityService);
        inOrder.verify(universityMapper).toEntity(requestDto);
        inOrder.verify(universityService).save(entity);
        inOrder.verify(universityMapper).toResponseDto(entity);
    }

    @Test
    void updateUniversity_shouldReturnResponseDto_whenUniversityExists() {
        when(universityService.findById(id)).thenReturn(Optional.of(entity));
        when(universityService.save(entity)).thenReturn(entity);
        when(universityMapper.toResponseDto(entity)).thenReturn(responseDto);

        final var result = universityWebFacade.updateUniversity(id, requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(universityMapper).updateEntity(requestDto, entity);
        verify(universityService).save(entity);
        verify(universityMapper).toResponseDto(entity);
    }

    @Test
    void updateUniversity_shouldThrowResourceNotFoundException_whenUniversityNotFound() {
        when(universityService.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universityWebFacade.updateUniversity(id, requestDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void getAllUniversities_shouldReturnListOfResponseDtos() {
        final var entities = List.of(entity);
        when(universityService.findAll()).thenReturn(entities);
        when(universityMapper.toResponseDtoList(entities)).thenReturn(List.of(responseDto));

        final var result = universityWebFacade.getAllUniversities();

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getUniversityById_shouldReturnResponseDto_whenUniversityExists() {
        when(universityService.findById(id)).thenReturn(Optional.of(entity));
        when(universityMapper.toResponseDto(entity)).thenReturn(responseDto);

        final var result = universityWebFacade.getUniversityById(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getUniversityById_shouldThrowResourceNotFoundException_whenUniversityNotFound() {
        when(universityService.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universityWebFacade.getUniversityById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void deleteUniversity_shouldDeleteUniversity_whenUniversityExists() {
        when(universityService.findById(id)).thenReturn(Optional.of(entity));

        universityWebFacade.deleteUniversity(id);

        verify(universityService).delete(entity);
    }

    @Test
    void deleteUniversity_shouldThrowResourceNotFoundException_whenUniversityNotFound() {
        when(universityService.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universityWebFacade.deleteUniversity(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }*/
}
