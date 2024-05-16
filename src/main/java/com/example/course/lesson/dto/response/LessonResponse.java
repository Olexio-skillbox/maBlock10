package com.example.course.lesson.dto.response;

import com.example.course.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private Long courseId;

    public static LessonResponse of(LessonEntity lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .courseId(lesson.getCourseId())
                .build();
    }
}
