package com.example.course.lesson.dto.response;

import com.example.course.course.dto.response.CourseResponse;
import com.example.course.course.entity.CourseEntity;
import com.example.course.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonFullResponse {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private CourseResponse course;

    public static LessonFullResponse of(LessonEntity lesson, CourseEntity course) {
        return LessonFullResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .courseId(lesson.getCourseId())
                .course(CourseResponse.of(course))
                .build();
    }
}
