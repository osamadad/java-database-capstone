package com.project.back_end.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prescriptions")
@Data
public class Prescription {
    @Id
    private String id;
    @NotNull
    @Size(min = 3, max = 100)
    private String patientName;
    @NotNull
    private Long appointmentId;
    @NotNull
    @Size(min = 3, max = 100)
    private String medication;
    @Size(max = 200)
    private String doctorNotes;
}