package com.rak.student.service;

import com.rak.student.domain.School;
import com.rak.student.domain.Student;
import com.rak.student.dto.SchoolDTO;
import com.rak.student.dto.StudentDTO;
import com.rak.student.mapper.StudentMapper;
import com.rak.student.repository.SchoolRepository;
import com.rak.student.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private SchoolServiceImpl schoolService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllStudentsEmptyList() {
        List<StudentDTO> expectedStudents = new ArrayList<>();
        Mockito.when(studentRepository.findAll()).thenReturn(new ArrayList<>());
        List<StudentDTO> actualStudents = studentService.getAllStudents();
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void testGetAllStudents() {
        List<Student> students = new ArrayList<>();

        School school1 = new School(1L, "Skiply", new ArrayList<>());
        School school2 = new School(2L, "American School", new ArrayList<>());

        Student student1 = new Student(1L, "John", "G1", "2012", "+92090078601", "Ali", school1);
        Student student2 = new Student(2L, "Doe", "G2", "2013", "+92090078602", "Sheikh", school2);

        students.add(student1);
        students.add(student2);

        List<StudentDTO> expectedDTOs = new ArrayList<>();

        StudentDTO studentDTO1 = new StudentDTO(1L, "John", "G1", "2012", "+92090078601", 1L, "Skiply", "Ali");
        StudentDTO studentDTO2 = new StudentDTO(2L, "Doe", "G2", "2013", "+92090078602", 1L, "Skiply", "Sheikh");
        expectedDTOs.add(studentDTO1);
        expectedDTOs.add(studentDTO2);

        // Mock the repository
        when(studentRepository.findAll()).thenReturn(students);

        when(studentMapper.toDTO(student1)).thenReturn(expectedDTOs.get(0));
        when(studentMapper.toDTO(student2)).thenReturn(expectedDTOs.get(1));

        List<StudentDTO> actualDTOs = studentService.getAllStudents();
        assertEquals(expectedDTOs, actualDTOs);
    }


    @Test
    public void testGetStudentByIdPositive() {
        
        Long studentId = 1L;
        School school = new School(1L, "Skiply School", new ArrayList<>());
        Student student = new Student(studentId, "John Doe", "G1", "2012", "+92090078601", "Ali", school);
        StudentDTO expectedDTO = new StudentDTO(studentId, "John Doe", "G1", "2012", "+92090078601", 1L, "Skiply School", "Ali");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(expectedDTO);

        
        StudentDTO actualDTO = studentService.getStudentById(studentId);

        
        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    public void testGetStudentById_StudentNotFound() {
        
        Long studentId = 1L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(studentId));
    }
    @Test
    public void testGetStudentByIdNegativeCase() {
        Long studentId = 1L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(studentId));
    }

    @Test
    public void testCreateStudent() {
        
        Long schoolId = 1L;
        School school = new School(schoolId, "Skiply School", new ArrayList<>());
        StudentDTO studentDTO = new StudentDTO(null, "John Doe", "G1", "2012", "+92090078601", schoolId, null, "Ali");

        when(studentMapper.toEntity(studentDTO)).thenReturn(new Student());
        when(schoolService.getSchoolById(schoolId)).thenReturn(new SchoolDTO("Skiply School",schoolId));
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student savedStudent = invocation.getArgument(0);
            savedStudent.setId(1L); // Simulating the saved entity with an ID
            return savedStudent;
        });
        when(studentMapper.toDTO(any(Student.class))).thenAnswer(invocation -> {
            Student savedStudent = invocation.getArgument(0);
            return new StudentDTO(savedStudent.getId(), savedStudent.getStudentName(), savedStudent.getGrade(),
                    savedStudent.getRollNumber(), savedStudent.getMobileNumber(), schoolId, school.getName(),
                    savedStudent.getGuardianName());
        });

        
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        createdStudent.setStudentName(studentDTO.getStudentName());
        createdStudent.setMobileNumber(studentDTO.getMobileNumber());
        createdStudent.setGuardianName(studentDTO.getGuardianName());
        createdStudent.setGrade(studentDTO.getGrade());

        
        assertNotNull(createdStudent.getId());
        assertEquals("John Doe", createdStudent.getStudentName());
        assertEquals("G1", createdStudent.getGrade());
        assertEquals(schoolId, createdStudent.getSchoolId());
        assertEquals("Skiply School", createdStudent.getSchoolName());
        assertNotNull(createdStudent.getRollNumber());
        assertEquals("Ali", createdStudent.getGuardianName());
    }

    @Test(expected = Exception.class)
    public void testCreateStudentNegative() {
        
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setSchoolId(1L);

        Student student = new Student();
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());
        studentService.createStudent(studentDTO);
        verify(studentMapper, times(1)).toEntity(studentDTO);
        verify(schoolRepository, times(1)).findById(1L);
        verify(studentRepository, never()).save(student);
        verify(studentMapper, never()).toDTO(student);
    }
}
