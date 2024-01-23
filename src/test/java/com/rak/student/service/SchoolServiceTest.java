package com.rak.student.service;

import com.rak.student.domain.School;
import com.rak.student.dto.SchoolDTO;
import com.rak.student.mapper.SchoolMapper;
import com.rak.student.repository.SchoolRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolServiceImpl schoolService;

    @Mock
    private SchoolMapper schoolMapper;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSchools() {

        List<School> school = new ArrayList<>();
        school.add(new School(1L, "SKIPLY", new ArrayList<>()));
        school.add(new School(2L, "American Lyceum", new ArrayList<>()));
        List<SchoolDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new SchoolDTO("Skiply", 1L));

        expectedDTOs.add(new SchoolDTO("American School", 2L));

        when(schoolRepository.findAll()).thenReturn(school);

        when(schoolMapper.toDTO(school.get(0))).thenReturn(expectedDTOs.get(0));
        when(schoolMapper.toDTO(school.get(1))).thenReturn(expectedDTOs.get(1));

        
        List<SchoolDTO> actualDTOs = schoolService.getAllSchools();

        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertEquals("Skiply", expectedDTOs.get(0).getName());
        assertEquals("American School", expectedDTOs.get(1).getName());


    }

    @Test
    public void getAllSchoolsEmptyList() {

        List<School> schools = new ArrayList<>();
        when(schoolRepository.findAll()).thenReturn(schools);


        List<SchoolDTO> result = schoolService.getAllSchools();


        assertEquals(0, result.size());
    }

    @Test
    public void getSchoolById() {
        Long schoolId = 1L;
        School school = new School(1L, "SKIPLY", new ArrayList<>());
        SchoolDTO expectedDto = new SchoolDTO("SKIPLY", 1L);

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(schoolMapper.toDTO(school)).thenReturn(expectedDto);

        SchoolDTO result = schoolService.getSchoolById(schoolId);

        verify(schoolRepository).findById(schoolId);
        verify(schoolMapper).toDTO(school);

        assertEquals(expectedDto, result);
    }

    @Test
    public void getSchoolByInValidId() {

        Long schoolId = 1L;

        MockitoAnnotations.initMocks(this);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());


        SchoolDTO actualDto = schoolService.getSchoolById(schoolId);


        assertNull(actualDto);
    }

    @Test
    public void createSchool() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setName("Skiply");

        School school = new School();
        school.setName("Skiply");

        when(schoolMapper.toEntity(schoolDTO)).thenReturn(school);

        schoolService.createSchool(schoolDTO);
        verify(schoolMapper).toEntity(schoolDTO);
        verify(schoolRepository).save(school);
        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    public void createSchoolNegative() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setName(null);
        SchoolDTO result = schoolService.createSchool(schoolDTO);
        assertNull(result);
    }

    @Test
    public void updateSchool() {
        
        Long schoolId = 1L;
        SchoolDTO updatedSchoolDTO = new SchoolDTO();
        updatedSchoolDTO.setName("Updated School Name");
        updatedSchoolDTO.setId(schoolId);
        School existingSchool = new School();
        existingSchool.setId(schoolId);
        existingSchool.setName("Original School Name");
        when(schoolMapper.toDTO(existingSchool)).thenReturn(updatedSchoolDTO);

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(existingSchool));
        when(schoolRepository.save(existingSchool)).thenReturn(existingSchool);

        
        SchoolDTO result = schoolService.updateSchool(schoolId, updatedSchoolDTO);

        
        Assertions.assertEquals(updatedSchoolDTO.getName(), result.getName());
        verify(schoolRepository, times(1)).findById(schoolId);
        verify(schoolRepository, times(1)).save(existingSchool);
    }

    @Test
    public void updateSchoolNotFound() {
        
        Long schoolId = 1L;
        SchoolDTO updatedSchoolDTO = new SchoolDTO();
        updatedSchoolDTO.setName("Updated School Name");

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            schoolService.updateSchool(schoolId, updatedSchoolDTO);
        });
        verify(schoolRepository, times(1)).findById(schoolId);
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    public void testDeleteSchool() {
        Long schoolId = 1L;
        Mockito.when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(new School(schoolId, "Test School",new ArrayList<>())));

        schoolService.deleteSchool(schoolId);

        Mockito.verify(schoolRepository, Mockito.times(1)).deleteById(schoolId);
    }


    @Test(expected = ResponseStatusException.class)
    public void testDeleteSchoolNotFound() {
        Long schoolId = 1L;
        Mockito.when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        schoolService.deleteSchool(schoolId);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteSchoolNullId() {
        schoolService.deleteSchool(null);
    }


}