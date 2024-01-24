package com.rak.student.service;

import com.rak.student.domain.School;
import com.rak.student.domain.Student;
import com.rak.student.dto.StudentDTO;
import com.rak.student.mapper.StudentMapper;
import com.rak.student.repository.SchoolRepository;
import com.rak.student.repository.StudentRepository;
import com.rak.student.util.Utility;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final StudentMapper studentMapper;

    /**
     * It retrieves all students from the student repository, maps them to StudentDTO objects using the studentMapper,
     * and returns a list of StudentDTOs.
     */
    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(studentMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param studentId The ID of the student to retrieve.
     * @return The StudentDTO object representing the student.
     * @throws ResponseStatusException if the student is not found.
     */
    @Override
    public StudentDTO getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found against studentId" + studentId));
    }

    /**
     * Creates a new student.
     *
     * @param studentDTO The StudentDTO object representing the student to create.
     * @return The StudentDTO object representing the created student.
     */
    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        student.setSchool(getSchoolOrThrowException(studentDTO.getSchoolId()));
        student.setRollNumber(String.valueOf(Utility.generateRandom4DigitNumber()));
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    /**
     * Updates an existing student.
     *
     * @param studentId         The ID of the student to update.
     * @param updatedStudentDTO The updated StudentDTO object.
     * @return The StudentDTO object representing the updated student.
     * @throws EntityNotFoundException if the student is not found.
     */
    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO updatedStudentDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        existingStudent.setStudentName(updatedStudentDTO.getStudentName());
        existingStudent.setGrade(updatedStudentDTO.getGrade());
        existingStudent.setMobileNumber(updatedStudentDTO.getMobileNumber());

        if (!existingStudent.getSchool().getId().equals(updatedStudentDTO.getSchoolId())) {
            existingStudent.setSchool(getSchoolOrThrowException(updatedStudentDTO.getSchoolId()));
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    /**
     * Deletes a student by their ID.
     *
     * @param studentId The ID of the student to delete.
     * @throws ResponseStatusException if the student is not found.
     */
    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.findById(studentId).ifPresentOrElse(x -> {
            schoolRepository.deleteById(x.getId());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found with id: " + studentId);
        });
    }

    /**
     * Retrieves a student by their roll number asynchronously.
     *
     * @param rollNo The roll number of the student to retrieve.
     * @return A CompletableFuture containing the StudentDTO object representing the student.
     * @throws ResponseStatusException if the student is not found.
     */
    @Override
    @CircuitBreaker(name = "getStudentApiCircuitBreaker", fallbackMethod = "fallbackStudentInfo")
    @Retry(name = "getStudentApiRetry", fallbackMethod = "fallbackStudentInfo")
    public CompletableFuture<StudentDTO> getStudentByRollNo(String rollNo) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate some processing time
//            try {
//                Thread.sleep(5000L);
//            } catch (InterruptedException e) {
//                // Handle interruption
//
//                Thread.currentThread().interrupt();
//            }
            // simulate circuit breaker
//            throw new RuntimeException("Simulated failure");

            return studentRepository.findFirstByRollNumber(rollNo)
                    .map(studentMapper::toDTO)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found against rollNo: " + rollNo));
        });
    }

    /**
     * Retrieves a school by its ID or throws an exception if not found.
     *
     * @param schoolId The ID of the school to retrieve.
     * @return The School object representing the school.
     * @throws ResponseStatusException if the school is not found.
     */
    private School getSchoolOrThrowException(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id: " + schoolId));
    }

    /**
     * Fallback method for the getStudentByRollNo method.
     *
     * @param e The exception that triggered the fallback.
     * @return A CompletableFuture containing null as a fallback value.
     */
    private CompletableFuture<StudentDTO> fallbackStudentInfo(Exception e) {
        return CompletableFuture.completedFuture(null);
    }
}

