package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(
            @PathVariable String token) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        return patientService.getPatientDetails(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createPatient(
            @Valid @RequestBody Patient patient) {

        int result = patientService.createPatient(patient);

        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Patient created successfully");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create patient");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody Patient patient) {

        return service.validatePatientLogin(patient);
    }

    @GetMapping("/{id}/appointments/{token}/{user}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long id,
            @PathVariable String token,
            @PathVariable String user) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, user);

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        return patientService.getPatientAppointment(id);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> filterPatientAppointment(
            @RequestParam String condition,
            @RequestParam(required = false) String name,
            @RequestParam String token) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        // Filtering implementation depends on how the patient's ID
        // is obtained from your existing Service methods.
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("Filtering endpoint implementation depends on patient ID retrieval");
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.


// 2. Autowire Dependencies:
//    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
//    - Inject the shared `Service` class for tasks like token validation and login authentication.


// 3. Define the `getPatient` Method:
//    - Handles HTTP GET requests to retrieve patient details using a token.
//    - Validates the token for the `"patient"` role using the shared service.
//    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.


// 4. Define the `createPatient` Method:
//    - Handles HTTP POST requests for patient registration.
//    - Accepts a validated `Patient` object in the request body.
//    - First checks if the patient already exists using the shared service.
//    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.


// 5. Define the `login` Method:
//    - Handles HTTP POST requests for patient login.
//    - Accepts a `Login` DTO containing email/username and password.
//    - Delegates authentication to the `validatePatientLogin` method in the shared service.
//    - Returns a response with a token or an error message depending on login success.


// 6. Define the `getPatientAppointment` Method:
//    - Handles HTTP GET requests to fetch appointment details for a specific patient.
//    - Requires the patient ID, token, and user role as path variables.
//    - Validates the token using the shared service.
//    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.


// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the shared service and returns the filtered result.



}


