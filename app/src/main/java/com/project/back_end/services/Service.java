package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public ResponseEntity<?> validateToken(String token, String role) {
        try {
            if (tokenService.validateToken(token, role)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    public ResponseEntity<?> validateAdmin(Admin admin) {
        try {
            Admin existingAdmin =
                    adminRepository.findAdminByUsername(admin.getUsername());

            if (existingAdmin!=null) {
                if (existingAdmin.getPassword().equals(admin.getPassword())) {
                    String token = tokenService.generateToken(existingAdmin.getUsername());
                    return ResponseEntity.ok(token);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    public List<Doctor> filterDoctor(String name, String specialty, String time) {

        if (name == null && specialty == null && time == null) {
            return doctorRepository.findAll();
        }

        if (name != null && specialty != null && time != null) {
            return doctorRepository.findDoctorBySpecialtyIgnoreCase(specialty);
        }

        return doctorRepository.findAll();

        // Handle the other filter combinations...
    }

    public int validateAppointment(Long doctorId, LocalDateTime requestedTime) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (doctor.isEmpty()) {
            return -1;
        }

        LocalDateTime requestedEnd = requestedTime.plusHours(1);

        List<Appointment> appointments =
                appointmentRepository
                        .getAppointmentByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                requestedTime,
                                requestedEnd
                        );

        if (!appointments.isEmpty()) {
            return 0;
        }

        return 1;
    }

    public boolean validatePatient(Patient patient) {

        Patient existingPatient =
                patientRepository.findPatientByEmailOrPhone(
                        patient.getEmail(),
                        patient.getPhone()
                );

        return existingPatient==null;
    }

    public ResponseEntity<?> validatePatientLogin(Patient patient) {
        try {
            Patient existingPatient =
                    patientRepository.findPatientByEmail(patient.getEmail());

            if (existingPatient!=null) {
                Patient foundPatient = existingPatient;

                if (existingPatient.getPassword().equals(patient.getPassword())) {
                    String token =
                            tokenService.generateToken(existingPatient.getEmail());

                    return ResponseEntity.ok(token);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

//    public List<Appointment> filterPatient(
//            String token,
//            String condition,
//            String doctorName) {
//
//        String email = tokenService.extractEmail(token);
//
//        if (condition != null && doctorName != null) {
//            return patientService
//                    .filterAppointments(email, condition, doctorName);
//        }
//
//        if (condition != null) {
//            return patientService
//                    .filterAppointmentsByCondition(email, condition);
//        }
//
//        if (doctorName != null) {
//            return patientService
//                    .filterAppointmentsByDoctorName(email, doctorName);
//        }
//
//        return patientService.get(email);
//    }
// 3. **validateToken Method**
// This method checks if the provided JWT token is valid for a specific user. It uses the TokenService to perform the validation.
// If the token is invalid or expired, it returns a 401 Unauthorized response with an appropriate error message. This ensures security by preventing
// unauthorized access to protected resources.

// 4. **validateAdmin Method**
// This method validates the login credentials for an admin user.
// - It first searches the admin repository using the provided username.
// - If an admin is found, it checks if the password matches.
// - If the password is correct, it generates and returns a JWT token (using the admin’s username) with a 200 OK status.
// - If the password is incorrect, it returns a 401 Unauthorized status with an error message.
// - If no admin is found, it also returns a 401 Unauthorized.
// - If any unexpected error occurs during the process, a 500 Internal Server Error response is returned.
// This method ensures that only valid admin users can access secured parts of the system.

// 5. **filterDoctor Method**
// This method provides filtering functionality for doctors based on name, specialty, and available time slots.
// - It supports various combinations of the three filters.
// - If none of the filters are provided, it returns all available doctors.
// This flexible filtering mechanism allows the frontend or consumers of the API to search and narrow down doctors based on user criteria.

// 6. **validateAppointment Method**
// This method validates if the requested appointment time for a doctor is available.
// - It first checks if the doctor exists in the repository.
// - Then, it retrieves the list of available time slots for the doctor on the specified date.
// - It compares the requested appointment time with the start times of these slots.
// - If a match is found, it returns 1 (valid appointment time).
// - If no matching time slot is found, it returns 0 (invalid).
// - If the doctor doesn’t exist, it returns -1.
// This logic prevents overlapping or invalid appointment bookings.

// 7. **validatePatient Method**
// This method checks whether a patient with the same email or phone number already exists in the system.
// - If a match is found, it returns false (indicating the patient is not valid for new registration).
// - If no match is found, it returns true.
// This helps enforce uniqueness constraints on patient records and prevent duplicate entries.

// 8. **validatePatientLogin Method**
// This method handles login validation for patient users.
// - It looks up the patient by email.
// - If found, it checks whether the provided password matches the stored one.
// - On successful validation, it generates a JWT token and returns it with a 200 OK status.
// - If the password is incorrect or the patient doesn't exist, it returns a 401 Unauthorized with a relevant error.
// - If an exception occurs, it returns a 500 Internal Server Error.
// This method ensures only legitimate patients can log in and access their data securely.

// 9. **filterPatient Method**
// This method filters a patient's appointment history based on condition and doctor name.
// - It extracts the email from the JWT token to identify the patient.
// - Depending on which filters (condition, doctor name) are provided, it delegates the filtering logic to PatientService.
// - If no filters are provided, it retrieves all appointments for the patient.
// This flexible method supports patient-specific querying and enhances user experience on the client side.


}
