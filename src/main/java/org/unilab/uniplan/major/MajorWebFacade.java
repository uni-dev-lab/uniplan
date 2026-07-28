package org.unilab.uniplan.major;

import static org.unilab.uniplan.utils.ErrorConstants.MAJOR_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class MajorWebFacade {

    private final MajorService majorService;
    private final MajorMapper majorMapper;
    private final MajorValidator majorValidator;

    @Transactional
    public void createMajor(final MajorRequestDto requestDto) {
        majorValidator.validate(requestDto);

        final Major major = majorMapper.toEntity(requestDto);
        majorService.save(major);

        log.info("Created major with ID: {}", major.getId());
    }

    @Transactional(readOnly = true)
    public List<MajorResponseDto> getAllMajors() {
        return majorMapper.toResponseDtoList(majorService.getAll());
    }

    @Transactional(readOnly = true)
    public MajorResponseDto getMajorById(final UUID id) {
        final Major major = getMajorOrThrow(id);

        return majorMapper.toResponseDto(major);
    }

    @Transactional
    public void updateMajor(final UUID id,
                            final MajorRequestDto requestDto) {
        majorValidator.validate(requestDto);

        final Major major = getMajorOrThrow(id);
        majorMapper.updateEntityFromDto(requestDto, major);
        majorService.save(major);

        log.info("Updated major with ID: {}", id);
    }

    @Transactional
    public void deleteMajor(final UUID id) {
        final Major major = getMajorOrThrow(id);

        majorService.delete(major);

        log.info("Deleted major with ID: {}", id);
    }

    private Major getMajorOrThrow(final UUID id) {
        return majorService.getById(id)
                           .orElseThrow(() -> new ResourceNotFoundException(
                               MAJOR_NOT_FOUND.getMessage(String.valueOf(id))
                           ));
    }

    @Transactional(readOnly = true)
    public MajorCoursesResponseDto getMajorWithCoursesById(final UUID id) {
        final Major major = getMajorOrThrow(id);

        return majorMapper.toFullResponseDto(major);
    }

    @Transactional(readOnly = true)
    public List<MajorResponseDto> getMajorsByFacultyId(final UUID facultyId) {
        return majorMapper.toResponseDtoList(
            majorService.findAllMajorByFacultyId(facultyId)
        );
    }

    @Transactional(readOnly = true)
    public List<MajorCoursesResponseDto> getMajorsWithCoursesByFacultyId(final UUID facultyId) {
        return majorMapper.toFullResponseDtoList(
            majorService.findAllMajorWithCoursesByFacultyId(facultyId)
        );
    }
}