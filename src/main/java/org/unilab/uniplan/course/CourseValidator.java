package org.unilab.uniplan.course;

import static org.unilab.uniplan.utils.ErrorConstants.MAJOR_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.major.MajorRepository;

@Component
@RequiredArgsConstructor
public class CourseValidator {

    private final MajorRepository majorRepository;

    public void validateForCreate(final CourseRequestDto requestDto) {
        validateMajorExists(requestDto.majorId());
    }

    public void validateForUpdate(final CourseRequestDto requestDto) {
        validateMajorExists(requestDto.majorId());
    }

    private void validateMajorExists(final UUID majorId) {
        if (!majorRepository.existsById(majorId)) {
            throw new ResourceNotFoundException(
                MAJOR_NOT_FOUND.getMessage(String.valueOf(majorId))
            );
        }
    }
}