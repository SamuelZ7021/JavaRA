package com.riwi.academico.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Student email is required")
    @Email(message = "Student email must be a valid email address")
    @Size(max = 100, message = "Student email cannot exceed 100 characters")
    private String email;

    @NotNull(message = "Student active status is required")
    private Boolean active;

}
