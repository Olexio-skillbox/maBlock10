package com.example.course.student.controller;

import com.example.course.student.dto.request.EditStudentRequest;
import com.example.course.student.dto.request.RegistrationRequest;
import com.example.course.student.dto.response.StudentResponse;
import com.example.course.student.entity.StudentEntity;
import com.example.course.student.exceptions.BadRequestException;
import com.example.course.student.exceptions.StudentAlreadyExistException;
import com.example.course.student.exceptions.StudentNotFoundException;
import com.example.course.student.repository.StudentRepository;
import com.example.course.student.routes.StudentRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(StudentRoutes.REGISTRATION)
    public StudentResponse registration(@RequestBody RegistrationRequest request) throws StudentAlreadyExistException, BadRequestException {
        request.validate();

        Optional<StudentEntity> check = studentRepository.findByEmail(request.getEmail());
        if (check.isPresent()) throw new StudentAlreadyExistException();

        StudentEntity student = StudentEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        student = studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @PutMapping(StudentRoutes.EDIT)
    public StudentResponse edit(Principal principal, @RequestBody EditStudentRequest request) throws StudentNotFoundException {
        StudentEntity student = studentRepository
                .findByEmail(principal.getName()).orElseThrow(StudentNotFoundException::new);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @GetMapping(StudentRoutes.BY_ID)
    public StudentResponse findById(@PathVariable Long id) throws StudentNotFoundException {
        return studentRepository.findById(id).map(StudentResponse::of).orElseThrow(StudentNotFoundException::new);
    }

    @GetMapping(StudentRoutes.SEARCH)
    public List<StudentResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<StudentEntity> example = Example.of(StudentEntity.builder().firstName(query).lastName(query).build(), exampleMatcher);
        return studentRepository.findAll(example, pageable).stream().map(StudentResponse::of).collect(Collectors.toList());
    }
}
