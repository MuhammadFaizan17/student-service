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

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(studentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found against studentId" + studentId));
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        student.setSchool(getSchoolOrThrowException(studentDTO.getSchoolId()));
        student.setRollNumber(String.valueOf(Utility.generateRandom4DigitNumber()));
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO updatedStudentDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        existingStudent.setStudentName(updatedStudentDTO.getStudentName());
        existingStudent.setGrade(updatedStudentDTO.getGrade());
        existingStudent.setMobileNumber(updatedStudentDTO.getMobileNumber());

        // Update school if schoolId is changed
        if (!existingStudent.getSchool().getId().equals(updatedStudentDTO.getSchoolId())) {
            existingStudent.setSchool(getSchoolOrThrowException(updatedStudentDTO.getSchoolId()));
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.findById(studentId).ifPresentOrElse(x -> {
            schoolRepository.deleteById(x.getId());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found with id: " + studentId);
        });
    }


    //    @Override
//    @CircuitBreaker(name = "getStudentApiCircuitBreaker", fallbackMethod = "fallbackStudentInfo")
//    public StudentDTO getStudentByRollNo(String rollNo) throws InterruptedException {
//        Thread.sleep(5000000L);
//        return studentRepository.findFirstByRollNumber(rollNo)
//                .map(studentMapper::toDTO)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found against rollNo: " + rollNo));
//    }
    @Override
    @CircuitBreaker(name = "getStudentApiCircuitBreaker", fallbackMethod = "fallbackStudentInfo")
    @Retry(name = "getStudentApiRetry", fallbackMethod = "fallbackStudentInfo")
    public CompletableFuture<StudentDTO> getStudentByRollNo(String rollNo) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate some processing time
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                // Handle interruption

                Thread.currentThread().interrupt();
            }
            // simulate circuit breaker
//            throw new RuntimeException("Simulated failure");

            return studentRepository.findFirstByRollNumber(rollNo)
                    .map(studentMapper::toDTO)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "student not found against rollNo: " + rollNo));
        });
    }


    private School getSchoolOrThrowException(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id: " + schoolId));
    }

    private CompletableFuture<StudentDTO> fallbackStudentInfo(Exception e) {
        return CompletableFuture.completedFuture(null);
    }
}

