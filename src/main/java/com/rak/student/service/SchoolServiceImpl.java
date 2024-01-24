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

    /**
     * Retrieves all schools from the database and converts them to DTOs.
     *
     * @return List of SchoolDTOs
     */

    @Override
    public List<SchoolDTO> getAllSchools() {
        List<School> schools = schoolRepository.findAll();
        return schools.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a school by its ID.
     *
     * @param schoolId The ID of the school to retrieve.
     * @return The SchoolDTO object representing the retrieved school, or null if not found.
     */
    @Override
    public SchoolDTO getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .map(mapper::toDTO)
                .orElse(null);
    }

    /**
     * Creates a new school with the given details.
     *
     * @param schoolDTO The details of the school to be created.
     * @return The newly created school.
     */
    @Override
    public SchoolDTO createSchool(SchoolDTO schoolDTO) {
        School school = mapper.toEntity(schoolDTO);
        School savedSchool = schoolRepository.save(school);
        return mapper.toDTO(savedSchool);
    }

    /**
     * Updates a school with the given schoolId using the information from the updatedSchoolDTO.
     * If the school with the given schoolId does not exist, a ResponseStatusException is thrown.
     * Returns the updated school as a SchoolDTO object.
     */
    @Override
    public SchoolDTO updateSchool(Long schoolId, SchoolDTO updatedSchoolDTO) {
        School existingSchool = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id:" + schoolId));

        existingSchool.setName(updatedSchoolDTO.getName());

        School updatedSchool = schoolRepository.save(existingSchool);
        return mapper.toDTO(updatedSchool);
    }

    /**
     * This is an implementation of the deleteSchool method in the SchoolService interface.
     * It deletes a school with the specified schoolId from the school repository.
     * If the school is not found, it throws a ResponseStatusException with a BAD_REQUEST status and an error message.
     */
    @Override
    public void deleteSchool(Long schoolId) {
        schoolRepository.findById(schoolId).ifPresentOrElse(x -> {
            schoolRepository.deleteById(x.getId());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "School not found with id: " + schoolId);
        });
    }
}

