package com.example.course.course.dto.response;

import com.example.course.course.entity.CourseEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CourseResponse extends CourseEntity {
    public static CourseResponse of(CourseEntity course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .build();
    }
}
