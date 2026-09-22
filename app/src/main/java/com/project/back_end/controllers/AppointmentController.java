package com.project.back_end.controllers;


import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    @GetMapping("/{doctorId}/{date}/{patientName}")
    public ResponseEntity<?> getAppointments(
            @PathVariable Long doctorId,
            @PathVariable LocalDate date,
            @PathVariable(required = false) String patientName
            ) {

//        ResponseEntity<?> tokenResponse =
//                service.validateToken(token, "doctor");
//
//        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
//            return tokenResponse;
//        }

        return ResponseEntity.ok(
                appointmentService.getAppointments(
                        doctorId,
                        date,
                        patientName
                )
        );
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(
            @Valid @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        int validation = service.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime()
        );

        if (validation == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Doctor not found");
        }

        if (validation == 0) {
            return ResponseEntity.badRequest()
                    .body("Doctor is not available at this time");
        }

        int result = appointmentService.bookAppointment(appointment);

        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Appointment booked successfully");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to book appointment");
    }

    @PutMapping("/{appointmentId}/{patientId}/{token}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long patientId,
            @Valid @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        String result = appointmentService.updateAppointment(
                appointmentId,
                patientId,
                appointment.getAppointmentTime()
        );

        if ("Appointment updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        }

        if ("Appointment not found".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        if ("Patient does not own this appointment".equals(result)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(result);
        }

        if ("Doctor is not available at this time".equals(result)) {
            return ResponseEntity.badRequest()
                    .body(result);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
    }

    @DeleteMapping("/{appointmentId}/{patientId}/{token}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long patientId,
            @PathVariable String token) {

        ResponseEntity<?> tokenResponse =
                service.validateToken(token, "patient");

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse;
        }

        String result = appointmentService.cancelAppointment(
                appointmentId,
                patientId
        );

        if ("Appointment cancelled successfully".equals(result)) {
            return ResponseEntity.ok(result);
        }

        if ("Appointment not found".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        if ("Patient does not own this appointment".equals(result)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(result);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
    }
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.


}
