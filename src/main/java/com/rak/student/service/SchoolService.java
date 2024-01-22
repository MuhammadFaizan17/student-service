package com.rak.student.service;

import com.rak.student.dto.SchoolDTO;

import java.util.List;

public interface SchoolService {

    List<SchoolDTO> getAllSchools();

    SchoolDTO getSchoolById(Long schoolId);

    SchoolDTO createSchool(SchoolDTO schoolDTO);

    SchoolDTO updateSchool(Long schoolId, SchoolDTO updatedSchoolDTO);

    void deleteSchool(Long schoolId);
}

