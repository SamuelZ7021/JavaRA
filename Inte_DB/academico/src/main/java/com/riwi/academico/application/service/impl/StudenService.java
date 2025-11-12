package com.riwi.academico.application.service.impl;


import com.riwi.academico.api.dtos.request.StudentRequest;
import com.riwi.academico.api.dtos.response.StudentResponse;
import com.riwi.academico.domain.entities.Student;
import com.riwi.academico.domain.ports.input.IStudentUseCase;
import com.riwi.academico.domain.ports.output.IStudentRepository;
import com.riwi.academico.infrastructure.mappers.StudentMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudenService implements IStudentUseCase {

    @Autowired
    private final IStudentRepository studentRepository;

    @Autowired
    private final StudentMapper studentMapper;


    @Override
    public StudentResponse create(StudentRequest request) {
        Student student = this.studentMapper.toStudent(request);

        Student savedStudent = this.studentRepository.save(student);
        return this.studentMapper.toStudentResponse(savedStudent);
    }

    @Override
    public StudentResponse get(Long id) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        return this.studentMapper.toStudentResponse(student);
    }

    @Override
    public List<StudentResponse> getAll() {
        List<Student> students = this.studentRepository.findAll();
        return students.stream()
                .map(this.studentMapper::toStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse update(Long id, StudentRequest request) {
        Student studentEx = this.studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID " + id));
        Student studentUpdate = this.studentMapper.toStudent(request);

        studentUpdate.setId(studentEx.getId());
        studentUpdate.setCreatedAt(studentEx.getCreatedAt());

        Student upStudent = this.studentRepository.save(studentUpdate);
        return this.studentMapper.toStudentResponse(upStudent);
    }

    @Override
    public void delete(Long id) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID " + id));
        this.studentRepository.deleteById(id);
    }
}
