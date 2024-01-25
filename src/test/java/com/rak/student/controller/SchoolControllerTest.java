package com.rak.student.controller;

import com.rak.student.domain.School;
import com.rak.student.dto.SchoolDTO;
import com.rak.student.service.SchoolService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchoolControllerTest {
    @InjectMocks
    private SchoolController schoolController;

    @Mock
    private SchoolService schoolService;

    String imageUrl = "https://play-lh.googleusercontent.com/1h4qUW1ECJ9bd27nDbkvc3uGhwFeFGt0yIGIRBQspXW24uJ0i34ePxMy-EVAXSX9Pg=w600-h300-pc0xffffff-pd";


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSchools() {
        List<School> schools = new ArrayList<>();
        schools.add(new School(1L, "Skiply", new ArrayList<>(), imageUrl));
        schools.add(new School(2L, "American Lyceum", new ArrayList<>(), imageUrl));
        List<SchoolDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new SchoolDTO("Skiply", 1L, imageUrl));

        expectedDTOs.add(new SchoolDTO("American School", 2L, imageUrl));

        when(schoolService.getAllSchools()).thenReturn(expectedDTOs);

        ResponseEntity<List<SchoolDTO>> response = schoolController.getAllSchools();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedDTOs.size(), response.getBody().size());
        verify(schoolService, times(1)).getAllSchools();


    }

    @Test
    public void testGetSchoolById() {
        Long schoolId = 1L;
        SchoolDTO school = new SchoolDTO("Skiply", schoolId, imageUrl);

        when(schoolService.getSchoolById(schoolId)).thenReturn(school);

        ResponseEntity<SchoolDTO> response = schoolController.getSchoolById(schoolId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(school, response.getBody());
    }

    @Test
    public void testCreateSchool() {
        SchoolDTO schoolDTO = new SchoolDTO("Skiply", 1L, imageUrl);
        SchoolDTO createdSchoolDTO = new SchoolDTO("Skiply", 1L, imageUrl);

        when(schoolService.createSchool(any())).thenReturn(createdSchoolDTO);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.createSchool(schoolDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Skiply", responseEntity.getBody().getName());

        verify(schoolService, times(1)).createSchool(eq(schoolDTO));
    }

    @Test
    public void testUpdateSchoolSuccess() {
        Long schoolId = 1L;
        SchoolDTO updatedSchoolDTO = new SchoolDTO("Updated School", schoolId, imageUrl);
        SchoolDTO updatedSchool = new SchoolDTO("Updated School", schoolId, imageUrl);

        when(schoolService.updateSchool(eq(schoolId), any())).thenReturn(updatedSchool);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.updateSchool(schoolId, updatedSchoolDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(schoolId, responseEntity.getBody().getId());
        assertEquals("Updated School", responseEntity.getBody().getName());

        verify(schoolService, times(1)).updateSchool(eq(schoolId), eq(updatedSchoolDTO));
    }

    @Test
    public void testUpdateSchoolNotFound() {
        Long schoolId = 1L;
        SchoolDTO updatedSchoolDTO = new SchoolDTO("Updated School", schoolId, imageUrl);

        when(schoolService.updateSchool(eq(schoolId), any())).thenReturn(null);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.updateSchool(schoolId, updatedSchoolDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(schoolService, times(1)).updateSchool(eq(schoolId), eq(updatedSchoolDTO));
    }

}
