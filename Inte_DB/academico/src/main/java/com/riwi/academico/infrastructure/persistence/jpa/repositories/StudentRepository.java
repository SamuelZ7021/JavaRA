package com.riwi.academico.infrastructure.persistence.jpa.repositories;

import com.riwi.academico.domain.entities.Student;
import com.riwi.academico.domain.ports.output.IStudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, IStudentRepository {
}
