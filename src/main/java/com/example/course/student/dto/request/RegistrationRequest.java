package com.example.course.student.dto.request;

import com.example.course.student.exceptions.BadRequestException;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public void validate() throws BadRequestException {
        if (email == null || email.isBlank()) throw new BadRequestException();
        if (password == null || password.isBlank()) throw new BadRequestException();
    }
}
