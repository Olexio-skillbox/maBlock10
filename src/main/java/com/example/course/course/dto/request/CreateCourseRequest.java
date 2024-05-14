package com.example.course.course.dto.request;

import com.example.course.course.entity.CourseEntity;
import lombok.Data;

@Data
public class CreateCourseRequest {
    private String title;
    private String description;

    public CourseEntity entity() {
        return CourseEntity.builder()
                .title(title)
                .description(description)
                .build();
    }

}
