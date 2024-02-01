package com.rak.student.repo;

import com.rak.student.domain.School;
import com.rak.student.domain.Student;
import com.rak.student.repository.SchoolRepository;
import com.rak.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class StudentRepoTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SchoolRepository schoolRepository;

    @BeforeEach
    public void setup() {
        School school = schoolRepository.save(School.builder().address("Dubai").name("skiply").build());

        studentRepository.save(new Student(1L, "John", "G1", "12345", "+92090078601", "Teacher 1", school));
    }

    @Test
    public void testFindFirstByRollNumberSuccess() {
        String rollNo = "12345";
        School school = (School.builder().id(1L).address("Dubai").name("skiply").build());
        Student expectedStudent = new Student(1L, "John", "G1", "12345", "+92090078601", "Teacher 1", school);

        Optional<Student> actualStudentOptional = studentRepository.findFirstByRollNumber(rollNo);

        assertTrue(actualStudentOptional.isPresent());
        Student actualStudent = actualStudentOptional.get();
        assertEquals(expectedStudent.getRollNumber(), actualStudent.getRollNumber());
        assertEquals(expectedStudent.getSchool().getId(), actualStudent.getSchool().getId());
        assertEquals(expectedStudent.getSchool().getName(), actualStudent.getSchool().getName());


    }

    @Test
    public void testFindFirstByRollNumberNotFound() {
        String rollNo = "1234";

        Optional<Student> actualStudentOptional = studentRepository.findFirstByRollNumber(rollNo);

        assertTrue(actualStudentOptional.isEmpty());
    }
}
