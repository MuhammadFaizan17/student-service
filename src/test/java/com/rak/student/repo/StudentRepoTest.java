package com.rak.student.repo;

import com.rak.student.domain.School;
import com.rak.student.domain.Student;
import com.rak.student.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
public class StudentRepoTest {
    @Mock
    private StudentRepository studentRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindFirstByRollNumberSuccess() {
        String rollNo = "12345";
        Student expectedStudent = new Student(1L, "John", "G1", "2022", "+92090078601", "Teacher 1", new School());
        when(studentRepository.findFirstByRollNumber(rollNo)).thenReturn(Optional.of(expectedStudent));

        Optional<Student> actualStudentOptional = studentRepository.findFirstByRollNumber(rollNo);

        assertTrue(actualStudentOptional.isPresent());
        Student actualStudent = actualStudentOptional.get();
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository, times(1)).findFirstByRollNumber(rollNo);
    }

    @Test
    public void testFindFirstByRollNumberNotFound() {
        String rollNo = "12345";
        when(studentRepository.findFirstByRollNumber(rollNo)).thenReturn(Optional.empty());

        Optional<Student> actualStudentOptional = studentRepository.findFirstByRollNumber(rollNo);

        assertTrue(actualStudentOptional.isEmpty());

        verify(studentRepository, times(1)).findFirstByRollNumber(rollNo);
    }
}
