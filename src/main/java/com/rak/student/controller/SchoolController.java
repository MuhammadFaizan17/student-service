package com.rak.student.controller;

import com.rak.student.dto.SchoolDTO;
import com.rak.student.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    @Operation(summary = "get list of all schools")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = schoolService.getAllSchools();
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(summary = "get school by id")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable Long id) {
        return new ResponseEntity<>(schoolService.getSchoolById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "create school")
    public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        SchoolDTO createdSchool = schoolService.createSchool(schoolDTO);
        return new ResponseEntity<>(createdSchool, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update school", description = "update school by school")
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable Long id, @RequestBody SchoolDTO updatedSchoolDTO) {
        SchoolDTO updatedSchool = schoolService.updateSchool(id, updatedSchoolDTO);
        return updatedSchool != null ?
                new ResponseEntity<>(updatedSchool, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

