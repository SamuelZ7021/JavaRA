package com.riwi.academico.infrastructure.persistence.jpa.repositories;

import com.riwi.academico.domain.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
