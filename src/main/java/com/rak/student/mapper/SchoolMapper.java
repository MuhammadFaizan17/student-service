package com.rak.student.mapper;

import com.rak.student.domain.School;
import com.rak.student.dto.SchoolDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolMapper {


    SchoolDTO toDTO(School school);

    School toEntity(SchoolDTO schoolDTO);

}
