package com.rak.student.mapper;

import com.rak.student.domain.Student;
import com.rak.student.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "schoolName", source = "school.name")
    StudentDTO toDTO(Student student);

    @Mapping(target = "school.id", source = "schoolId")
    Student toEntity(StudentDTO studentDTO);
}

