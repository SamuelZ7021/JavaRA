package com.riwi.academico.domain.ports.output;

import com.riwi.academico.domain.entities.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentRepository {
    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    void deleteById(Long id);

}
