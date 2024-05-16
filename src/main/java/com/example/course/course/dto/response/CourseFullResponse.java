package com.example.course.course.dto.response;

import com.example.course.course.entity.CourseEntity;
import com.example.course.lesson.dto.response.LessonResponse;
import com.example.course.lesson.entity.LessonEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class CourseFullResponse extends CourseEntity {
    private List<LessonResponse> lessons = new ArrayList<>();
    public static CourseFullResponse of(CourseEntity course, List<LessonEntity> lessonEntities) {
        return CourseFullResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .lessons(lessonEntities.stream().map(LessonResponse::of).collect(Collectors.toList()))
                .build();
    }
}
