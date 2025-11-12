package com.riwi.academico.infrastructure.mappers;


import com.riwi.academico.api.dtos.request.StudentRequest;
import com.riwi.academico.api.dtos.response.StudentResponse;
import com.riwi.academico.domain.entities.Student;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "enrollments", ignore = true)
    })
    Student toStudent(StudentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateStudentFromRequest(StudentRequest request, @MappingTarget Student student);
}
