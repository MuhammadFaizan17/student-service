package com.rak.student;

import com.rak.student.domain.School;
import com.rak.student.domain.Student;
import com.rak.student.dto.SchoolDTO;
import com.rak.student.dto.StudentDTO;
import com.rak.student.mapper.StudentMapper;
import com.rak.student.repository.StudentRepository;
import com.rak.student.service.SchoolService;
import com.rak.student.service.StudentService;
import com.rak.student.service.StudentServiceImpl;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolService schoolService;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentMapper = Mockito.mock(StudentMapper.class);
        schoolService = Mockito.mock(SchoolService.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        studentService = Mockito.mock(StudentServiceImpl.class);
    }

    @Test
    void testCreateStudent_SuccessfulCall() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setSchoolId(1L);

        Student student = new Student();
        student.setSchool(new School());
        student.setRollNumber("1234");

        Mockito.when(schoolService.getSchoolById(Mockito.anyLong()))
                .thenReturn(new SchoolDTO());
        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(student);
        Mockito.when(studentMapper.toDTO(Mockito.any(Student.class)))
                .thenReturn(studentDTO);

        // Act
        StudentDTO actualResult = studentService.createStudent(studentDTO);

        // Assert
        assertEquals(studentDTO, actualResult);
    }

    @Test
    void testCreateStudent_CircuitBreakerFallback() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setSchoolId(1L);

        // Simulate a circuit breaker open state
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        circuitBreakerRegistry.circuitBreaker("createStudentApiCircuitBreaker").transitionToOpenState();

        // Act
        StudentDTO actualResult = studentService.createStudent(studentDTO);

        // Assert
        assertEquals("Fallback User Information", actualResult);
    }
}
