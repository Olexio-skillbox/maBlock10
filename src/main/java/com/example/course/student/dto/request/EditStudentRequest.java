package com.example.course.student.dto.request;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class EditStudentRequest {
    private String firstName;
    private String lastName;
}
