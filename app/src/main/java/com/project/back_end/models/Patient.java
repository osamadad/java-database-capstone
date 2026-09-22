package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min =3, max=100)
    private String name;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min=6)
    private String password;
    @NotNull
    @Pattern(regexp="^[0-9]{10}$")
    private String phone;
    @NotNull
    @Size(max=255)
    private String address;
    private int status; // 0 = Scheduled, 1 = Completed
}
