package com.example.course.lesson.controller;

import com.example.course.course.entity.CourseEntity;
import com.example.course.course.exception.CourseNotFoundException;
import com.example.course.course.repository.CourseRepository;
import com.example.course.lesson.dto.request.CreateLessonRequest;
import com.example.course.lesson.dto.request.EditLessonRequest;
import com.example.course.lesson.dto.response.LessonFullResponse;
import com.example.course.lesson.dto.response.LessonResponse;
import com.example.course.lesson.entity.LessonEntity;
import com.example.course.lesson.exception.LessonNotFoundException;
import com.example.course.lesson.repository.LessonRepository;
import com.example.course.lesson.routes.LessonRoutes;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class LessonApiController {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @PostMapping(LessonRoutes.CREATE)
    public LessonFullResponse create(@RequestBody CreateLessonRequest request) throws CourseNotFoundException {
        CourseEntity course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(CourseNotFoundException::new);

        LessonEntity lesson = request.entity();
        lesson = lessonRepository.save(lesson);

        return LessonFullResponse.of(lesson, course);
    }
    @GetMapping(LessonRoutes.BY_ID)
    public LessonFullResponse findById(@PathVariable Long id) throws LessonNotFoundException, CourseNotFoundException {
        LessonEntity lesson = lessonRepository.findById(id).orElseThrow(LessonNotFoundException::new);
        CourseEntity course = courseRepository.findById(lesson.getCourseId()).orElseThrow(CourseNotFoundException::new);

        return LessonFullResponse.of(lesson, course);
    }
    @PutMapping(LessonRoutes.EDIT)
    public LessonFullResponse edit(@RequestBody EditLessonRequest request) throws CourseNotFoundException, LessonNotFoundException {
        LessonEntity lesson = lessonRepository.findById(request.getId()).orElseThrow(LessonNotFoundException::new);
        CourseEntity course = courseRepository.findById(lesson.getCourseId()).orElseThrow(CourseNotFoundException::new);

        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lessonRepository.save(lesson);

        return LessonFullResponse.of(lesson, course);
    }
    @DeleteMapping(LessonRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        lessonRepository.deleteById(id);
        return HttpStatus.OK.name();
    }
    @GetMapping(LessonRoutes.SEARCH)
    public List<LessonResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<LessonEntity> example = Example.of(LessonEntity.builder().title(query).description(query).build(), exampleMatcher);

        return lessonRepository
                .findAll(example, pageable)
                .stream()
                .map(LessonResponse::of)
                .collect(Collectors.toList());
    }
}
