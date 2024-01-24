package com.rak.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rak.student.enums.Grade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO implements Serializable {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("studentId")
    private Long id;

    @NotBlank(message = "studentName is mandatory")
    private String studentName;

    @NotBlank(message = "grade is mandatory")
    private String grade;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String rollNumber;

    @NotBlank(message = "mobileNumber is mandatory")
    private String mobileNumber;

    @NotNull(message = "schoolId is mandatory")
    private Long schoolId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String schoolName;

    @NotBlank(message = "guardianName is mandatory")
    private String guardianName;

    public void setGrade(String grade) {
        if (!Grade.isValid(grade))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid grade value must be like G1 to G10");
        this.grade = grade;
    }

}

