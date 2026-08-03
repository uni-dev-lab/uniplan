package org.unilab.uniplan.student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query("""
        SELECT s FROM Student s
        JOIN FETCH s.course c
        JOIN FETCH c.major m
        """)
    List<Student> findAllWithCourseAndMajor();

    @Query("""
        SELECT s FROM Student s
        JOIN FETCH s.course c
        JOIN FETCH c.major m
        WHERE s.id = :id
        """)
    Optional<Student> findByIdWithCourseAndMajor(@Param("id") UUID id);
}
