package org.unilab.uniplan.program;

import static org.unilab.uniplan.utils.ErrorConstants.COURSE_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.program.dto.ProgramRequestDto;

@Component
@RequiredArgsConstructor
public class ProgramValidator {

    private static final String PROGRAM_FOR_COURSE_EXISTS =
        "A program with course ID: %s already exists.";

    private final ProgramRepository programRepository;
    private final CourseRepository courseRepository;

    public void validateForCreate(final ProgramRequestDto request) {
        validateCourseExists(request.courseId());
        validateNoProgramExistsWithCourseId(request.courseId());
    }

    public void validateForUpdate(final UUID programId, final ProgramRequestDto request) {
        validateProgramExists(programId);
        validateCourseExists(request.courseId());
        validateNoOtherProgramExistsWithCourseId(request.courseId(), programId);
    }

    private void validateProgramExists(final UUID id) {
        if (!programRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                PROGRAM_NOT_FOUND.getMessage(String.valueOf(id)));
        }
    }

    private void validateCourseExists(final UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                COURSE_NOT_FOUND.getMessage(String.valueOf(id)));
        }
    }

    private void validateNoProgramExistsWithCourseId(final UUID courseId) {
        if (programRepository.existsByCourse_Id(courseId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                PROGRAM_FOR_COURSE_EXISTS
                    .formatted(courseId)
            );
        }
    }

    private void validateNoOtherProgramExistsWithCourseId(final UUID courseId,
                                                          final UUID programId) {
        if (programRepository.existsByCourse_IdAndIdNot(courseId, programId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                PROGRAM_FOR_COURSE_EXISTS
                    .formatted(courseId)
            );
        }
    }
}
