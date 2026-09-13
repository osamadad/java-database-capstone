package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final com.project.back_end.services.Service service;

    @Transactional
    public int bookAppointment(Appointment appointment){
        try {
            appointmentRepository.save(appointment);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }


    @Transactional
    public String updateAppointment(Long appointmentId, Long patientId,
                                    LocalDateTime newAppointmentTime) {
        try {
            Optional<Appointment> optionalAppointment =
                    appointmentRepository.findById(appointmentId);

            if (optionalAppointment.isEmpty()) {
                return "Appointment not found";
            }

            Appointment appointment = optionalAppointment.get();

            if (!appointment.getPatient().getId().equals(patientId)) {
                return "Patient does not own this appointment";
            }

            // Validate that the new appointment time is available
            int validation =
                    service.validateAppointment(
                            appointment.getDoctor().getId(),
                            newAppointmentTime
                    );

            if (validation != 1) {
                return "Doctor is not available at this time";
            }

            appointment.setAppointmentTime(newAppointmentTime);
            appointmentRepository.save(appointment);

            return "Appointment updated successfully";

        } catch (Exception e) {
            return "Failed to update appointment";
        }
    }

    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        try {
            Optional<Appointment> optionalAppointment =
                    appointmentRepository.findById(appointmentId);

            if (optionalAppointment.isEmpty()) {
                return "Appointment not found";
            }

            Appointment appointment = optionalAppointment.get();

            if (!appointment.getPatient().getId().equals(patientId)) {
                return "Patient does not own this appointment";
            }

            appointmentRepository.delete(appointment);

            return "Appointment cancelled successfully";

        } catch (Exception e) {
            return "Failed to cancel appointment";
        }
    }

    @Transactional
    public List<Appointment> getAppointments(
            Long doctorId,
            LocalDate date,
            String patientName) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        if (patientName == null || patientName.isBlank()) {
            return appointmentRepository
                    .getAppointmentByDoctorIdAndAppointmentTimeBetween(
                            doctorId,
                            startOfDay,
                            endOfDay
                    );
        }

        // Use your existing repository query for filtering by patient name.
        return appointmentRepository
                .getAppointmentsByDoctorIdAndPatientNameContainingIgnoreCaseAndAppointmentTimeBetween(
                        doctorId,
                        patientName,
                        startOfDay,
                        endOfDay
                );
    }

    @Transactional
    public int changeStatus(Long appointmentId, int status) {
        try {
            appointmentRepository.updateStatus(status,appointmentId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.

// 5. **Update Appointment Method**:
//    - This method is used to update an existing appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.

// 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.

// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.


}
