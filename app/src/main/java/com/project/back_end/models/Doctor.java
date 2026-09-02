package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 3, max = 100)
    private String name;
    @NotNull
    @Size(min = 3, max = 50)
    private String specialty;
    @Email
    @NotNull
    private String email;
    @Size(min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Pattern(regexp = "\\d{10}")
    private String phone;
    @ElementCollection
    private List<String> availableTimes;
}
