package com.rak.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDTO implements Serializable {

    @JsonProperty("schoolName")
    @NotBlank
    private String name;

    @JsonProperty("schoolId")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}

