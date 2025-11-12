package com.riwi.academico.api.controller;

import com.riwi.academico.domain.ports.input.IStudentUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
@Tag(name = "Students", description = "Enpoints form managing students")
public class StudentController {

    @Autowired
    private final IStudentUseCase studentUseCase;
}
