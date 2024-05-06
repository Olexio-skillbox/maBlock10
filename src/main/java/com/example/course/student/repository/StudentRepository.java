package com.example.course.student.repository;

import com.example.course.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByEmail(String email);
}
