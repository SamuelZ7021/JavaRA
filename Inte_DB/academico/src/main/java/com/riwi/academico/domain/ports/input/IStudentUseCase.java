package com.riwi.academico.domain.ports.input;

import com.riwi.academico.api.dtos.request.StudentRequest;
import com.riwi.academico.api.dtos.response.StudentResponse;

import java.util.List;

public interface IStudentUseCase {
    StudentResponse create(StudentRequest request);
    StudentResponse get(Long id);
    List<StudentResponse> getAll();
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
}
