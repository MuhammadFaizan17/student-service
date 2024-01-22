package com.rak.student.service;

import com.rak.student.domain.School;
import com.rak.student.dto.SchoolDTO;
import com.rak.student.mapper.SchoolMapper;
import com.rak.student.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolMapper mapper;

    @Override
    public List<SchoolDTO> getAllSchools() {
        List<School> schools = schoolRepository.findAll();
        return schools.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public SchoolDTO getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    public SchoolDTO createSchool(SchoolDTO schoolDTO) {
        School school = mapper.toEntity(schoolDTO);
        School savedSchool = schoolRepository.save(school);
        return mapper.toDTO(savedSchool);
    }

    @Override
    public SchoolDTO updateSchool(Long schoolId, SchoolDTO updatedSchoolDTO) {
        School existingSchool = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id: " + schoolId));

        existingSchool.setName(updatedSchoolDTO.getName());

        School updatedSchool = schoolRepository.save(existingSchool);
        return mapper.toDTO(updatedSchool);
    }

    @Override
    public void deleteSchool(Long schoolId) {
        schoolRepository.findById(schoolId).ifPresentOrElse(x -> {
            schoolRepository.deleteById(x.getId());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id: " + schoolId);
        });

    }
}

