package com.rak.student.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Arrays.stream;

@Getter
@NoArgsConstructor
public enum Grade {
    G1("G1"),
    G2("G2"),
    G3("G3"),
    G4("G4"),
    G5("G5"),
    G6("G6"),
    G7("G7"),
    G8("G8"),
    G9("G9"),
    G10("G10");

    String grade;

    Grade(String grade) {
        this.grade = grade;
    }

    public static boolean isValid(String grade) {
        return stream(values()).anyMatch(type -> type.getGrade().equalsIgnoreCase(grade));
    }

}

