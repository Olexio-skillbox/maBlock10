package com.example.course.student.dto.response;

import com.example.course.student.entity.StudentEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class StudentResponse {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static StudentResponse of(StudentEntity entity) {
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }
}
