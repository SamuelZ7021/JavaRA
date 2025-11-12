package com.riwi.academico.api.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private Boolean active;
    // private List<EnrollmentResponse> enrollments;
}
