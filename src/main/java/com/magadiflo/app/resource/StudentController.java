package com.magadiflo.app.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Martín"), new Student(2, "Gaspar"), new Student(3, "Alicia"),
            new Student(4, "Tinkler"), new Student(5, "Raúl"), new Student(6, "Abraham")
    );

    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student ".concat(studentId.toString()).concat(" does not exists")));
    }

}
