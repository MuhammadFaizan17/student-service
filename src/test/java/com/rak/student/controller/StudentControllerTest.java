package com.rak.student.controller;

import com.rak.student.dto.StudentDTO;
import com.rak.student.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentControllerTest {
    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    String imageUrl = "https://play-lh.googleusercontent.com/1h4qUW1ECJ9bd27nDbkvc3uGhwFeFGt0yIGIRBQspXW24uJ0i34ePxMy-EVAXSX9Pg=w600-h300-pc0xffffff-pd";

    @Test
    public void testGetAllStudents() {

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(new StudentDTO(1L, "John", "G1", "2022", "+92090078601", 1L, "School 1", "Teacher 1", imageUrl));
        expectedStudents.add(new StudentDTO(2L, "Doe", "G2", "2022", "+92090078602", 2L, "School 2", "Teacher 2", imageUrl));

        when(studentService.getAllStudents()).thenReturn(expectedStudents);


        ResponseEntity<List<StudentDTO>> responseEntity = studentController.getAllStudents();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedStudents.size(), responseEntity.getBody().size());
        assertEquals(expectedStudents, responseEntity.getBody());

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudentById() {

        Long studentId = 1L;
        StudentDTO expectedStudent = new StudentDTO(studentId, "John", "G1", "2022", "+92090078601", 1L, "School 1", "Teacher 1", imageUrl);

        when(studentService.getStudentById(studentId)).thenReturn(expectedStudent);


        StudentDTO result = studentController.getStudentById(studentId);


        assertNotNull(result);
        assertEquals(expectedStudent, result);

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    public void testCreateStudent() {

        StudentDTO inputStudent = new StudentDTO(null, "John", "G1", "2022", "+92090078601", 1L, "School 1", "Teacher 1", imageUrl);
        StudentDTO expectedCreatedStudent = new StudentDTO(1L, "John", "G1", "2022", "+92090078601", 1L, "School 1", "Teacher 1", imageUrl);

        when(studentService.createStudent(inputStudent)).thenReturn(expectedCreatedStudent);


        ResponseEntity<StudentDTO> response = studentController.createStudent(inputStudent);


        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        StudentDTO createdStudent = response.getBody();
        assertNotNull(createdStudent);
        assertEquals(expectedCreatedStudent, createdStudent);

        verify(studentService, times(1)).createStudent(inputStudent);
    }

    @Test
    public void testUpdateStudentSuccess() {
        Long studentId = 1L;
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "UpdatedName", "G2", "2023", "+92090078602", 1L, "School 1", "Teacher 1", imageUrl);
        StudentDTO expectedUpdatedStudent = new StudentDTO(studentId, "UpdatedName", "G2", "2023", "+92090078602", 1L, "School 1", "Teacher 1", imageUrl);

        when(studentService.updateStudent(studentId, updatedStudentDTO)).thenReturn(expectedUpdatedStudent);

        ResponseEntity<StudentDTO> response = studentController.updateStudent(studentId, updatedStudentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        StudentDTO updatedStudent = response.getBody();
        assertNotNull(updatedStudent);
        assertEquals(expectedUpdatedStudent, updatedStudent);

        verify(studentService, times(1)).updateStudent(studentId, updatedStudentDTO);
    }

    @Test
    public void testUpdateStudentNotFound() {

        Long studentId = 1L;
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "UpdatedName", "G2", "2023", "+92090078602", 1L, "School 1", "Teacher 1", imageUrl);

        when(studentService.updateStudent(studentId, updatedStudentDTO)).thenReturn(null);

        ResponseEntity<StudentDTO> response = studentController.updateStudent(studentId, updatedStudentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(studentService, times(1)).updateStudent(studentId, updatedStudentDTO);
    }

    @Test
    public void testGetStudentByRollNoSuccess() throws InterruptedException, ExecutionException {
        String rollNo = "12345";
        StudentDTO expectedStudent = new StudentDTO(1L, "John", "G1", "2022", "+92090078601", 1L, "School 1", "Teacher 1", imageUrl);

        when(studentService.getStudentByRollNo(rollNo)).thenReturn(CompletableFuture.completedFuture(expectedStudent));

        CompletableFuture<StudentDTO> responseFuture = studentController.getStudentByRollNo(rollNo);

        assertNotNull(responseFuture);

        StudentDTO actualStudent = responseFuture.get();

        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);

        verify(studentService, times(1)).getStudentByRollNo(rollNo);
    }

    @Test
    public void testGetStudentByRollNoException() throws InterruptedException {
        String rollNo = "12345";

        when(studentService.getStudentByRollNo(rollNo)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Error")));

        CompletableFuture<StudentDTO> responseFuture = studentController.getStudentByRollNo(rollNo);

        assertNotNull(responseFuture);

        assertThrows(ExecutionException.class, responseFuture::get);

        verify(studentService, times(1)).getStudentByRollNo(rollNo);
    }
}
