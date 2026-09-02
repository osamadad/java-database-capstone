package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Doctor doctor;
    @ManyToOne
    @NotNull
    private Patient patient;
    @Future
    private LocalDateTime appointmentTime;
    private int status; // 0 = Scheduled, 1 = Completed

    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }
    @Transient
    public LocalDate getAppointmentDate(){
        return appointmentTime.toLocalDate();
    }
    @Transient
    public LocalTime getAppointmentTimeOnly(){
        return appointmentTime.toLocalTime();
    }
}