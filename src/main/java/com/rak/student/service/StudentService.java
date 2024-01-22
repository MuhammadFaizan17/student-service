package com.rak.student.service;

import com.rak.student.dto.StudentDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StudentService {

    List<StudentDTO> getAllStudents();

    StudentDTO getStudentById(Long studentId);

    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(Long studentId, StudentDTO updatedStudentDTO);

    void deleteStudent(Long studentId);

    CompletableFuture<StudentDTO> getStudentByRollNo(String rollNo) throws InterruptedException;
}

