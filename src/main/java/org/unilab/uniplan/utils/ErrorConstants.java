package org.unilab.uniplan.utils;

import java.text.MessageFormat;

public enum ErrorConstants {

    UNIVERSITY_NOT_FOUND("University with ID {0} not found."),
    STUDENT_NOT_FOUND("Student with ID {0} not found."),
    ROOM_NOT_FOUND("Room with ID {0} not found."),
    PROGRAM_NOT_FOUND("Program with ID {0} not found."),
    MAJOR_NOT_FOUND("Major with ID {0} not found."),
    LECTOR_NOT_FOUND("Lector with ID {0} not found."),
    FACULTY_NOT_FOUND("Faculty with ID {0} not found."),
    DISCIPLINE_NOT_FOUND("Discipline with ID {0} not found."),
    DEPARTMENT_NOT_FOUND("Department with ID {0} not found."),
    COURSE_NOT_FOUND("Course with ID {0} not found."),
    CATEGORY_NOT_FOUND("Category with ID {0} not found."),
    COURSE_GROUP_NOT_FOUND("Course group with ID {0} not found."),
    STUDENT_GROUP_NOT_FOUND("Student group with ID {0} not found."),
    ROOM_CATEGORY_NOT_FOUND("Room category with ID {0} not found."),
    PROGRAM_DISCIPLINE_NOT_FOUND("Program discipline with ID {0} not found."),
    PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND("Program discipline lector with ID {0} not found.");

    private final String message;

    ErrorConstants(final String message) {
        this.message = message;
    }

    public String getMessage(final String id) {
        return MessageFormat.format(message, id);
    }
}
