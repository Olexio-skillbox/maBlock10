package com.example.course.course.controller;

import com.example.course.course.dto.request.CreateCourseRequest;
import com.example.course.course.dto.request.EditCourseRequest;
import com.example.course.course.dto.response.CourseFullResponse;
import com.example.course.course.dto.response.CourseResponse;
import com.example.course.course.entity.CourseEntity;
import com.example.course.course.exception.CourseNotFoundException;
import com.example.course.course.repository.CourseRepository;
import com.example.course.course.routes.CourseRoutes;
import com.example.course.lesson.entity.LessonEntity;
import com.example.course.lesson.repository.LessonRepository;
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
public class CourseApiController {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @PostMapping(CourseRoutes.CREATE)
    public CourseFullResponse create(@RequestBody CreateCourseRequest request) {
        CourseEntity course = courseRepository.save(request.entity());
        return CourseFullResponse.of(course, lessonRepository.findByCourseId(course.getId()));
    }

    @PutMapping(CourseRoutes.EDIT)
    public CourseFullResponse edit(@RequestBody EditCourseRequest request) throws CourseNotFoundException {
        // Получаем курс
        CourseEntity course = courseRepository.findById(request.getId()).orElseThrow(CourseNotFoundException::new);
        // Изменяем данные курса
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        courseRepository.save(course);
        return CourseFullResponse.of(course, lessonRepository.findByCourseId(course.getId()));
    }

    @GetMapping(CourseRoutes.BY_ID)
    public CourseFullResponse findById(@PathVariable Long id) throws CourseNotFoundException {
        CourseEntity course = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        return CourseFullResponse.of(course, lessonRepository.findByCourseId(course.getId()));
    }

    // Поиск курса
    @GetMapping(CourseRoutes.SEARCH)
    public List<CourseResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Поиск по полям
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        // Значения для поиска
        Example<CourseEntity> example = Example.of(
                CourseEntity.builder()
                .title(query).description(query).build(),
                exampleMatcher);

        return courseRepository.findAll(example, pageable).stream().map(CourseResponse::of).collect(Collectors.toList());
    }

    @DeleteMapping(CourseRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        List<LessonEntity> lessonEntities = lessonRepository.findByCourseId(id);
        for (LessonEntity lesson : lessonEntities) {
            lessonRepository.deleteById(lesson.getId());
        }

        courseRepository.deleteById(id);
        return HttpStatus.OK.name();
    }
}
