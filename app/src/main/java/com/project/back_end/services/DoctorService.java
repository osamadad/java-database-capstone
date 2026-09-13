package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Transactional
    public List<LocalTime> getDoctorAvailability(
            Long doctorId,
            LocalDate date) {

        Optional<Doctor> optionalDoctor =
                doctorRepository.findById(doctorId);

        if (optionalDoctor.isEmpty()) {
            return List.of();
        }

        Doctor doctor = optionalDoctor.get();

        List<LocalTime> availableTimes = doctor.getAvailableTimes()
                .stream()
                .map(LocalTime::parse)
                .toList();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments =
                appointmentRepository
                        .getAppointmentByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                startOfDay,
                                endOfDay
                        );

        List<LocalTime> bookedTimes = appointments.stream()
                .map(Appointment::getAppointmentTimeOnly)
                .toList();

        return availableTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    // 5. saveDoctor
    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor =
                    doctorRepository.findDoctorByEmail(doctor.getEmail());

            if (existingDoctor==null) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // 6. updateDoctor
    @Transactional
    public int updateDoctor(Long id, Doctor doctor) {
        try {
            Optional<Doctor> existingDoctor =
                    doctorRepository.findById(id);

            if (existingDoctor.isEmpty()) {
                return -1;
            }

            Doctor existing = existingDoctor.get();

            existing.setName(doctor.getName());
            existing.setSpecialty(doctor.getSpecialty());
            existing.setEmail(doctor.getEmail());
            existing.setPassword(doctor.getPassword());
            existing.setPhone(doctor.getPhone());
            existing.setAvailableTimes(doctor.getAvailableTimes());

            doctorRepository.save(existing);

            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // 7. getDoctors
    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // 8. deleteDoctor
    @Transactional
    public int deleteDoctor(Long doctorId) {
        try {
            Optional<Doctor> optionalDoctor =
                    doctorRepository.findById(doctorId);

            if (optionalDoctor.isEmpty()) {
                return -1;
            }

            Doctor doctor = optionalDoctor.get();

            List<Appointment> appointments =
                    appointmentRepository
                            .getAppointmentByDoctorIdAndAppointmentTimeBetween(
                                    doctorId,
                                    LocalDateTime.MIN,
                                    LocalDateTime.MAX
                            );

            appointmentRepository.deleteAll(appointments);
            doctorRepository.delete(doctor);

            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // 9. validateDoctor
    public ResponseEntity<?> validateDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor =
                    doctorRepository.findDoctorByEmail(doctor.getEmail());

            if (existingDoctor==null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            }

            if (!existingDoctor.getPassword().equals(doctor.getPassword())) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            }

            String token =
                    tokenService.generateToken(existingDoctor.getEmail());

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // 10. findDoctorByName
    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findDoctorByNameContainsIgnoreCase(name);
    }

    // 11. filterDoctorsByNameSpecilityandTime
    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String time) {

        List<Doctor> doctors =
                doctorRepository.findDoctorByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty
                );

        return filterDoctorByTime(doctors, time);
    }

    // 12. filterDoctorByTime
    @Transactional
    public List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String time) {

        if (time == null || time.isBlank()) {
            return doctors;
        }

        String period = time.toUpperCase();

        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes()
                        .stream()
                        .anyMatch(availableTime ->
                                isTimeInPeriod(LocalTime.parse(availableTime), period)))
                .collect(Collectors.toList());
    }

    // 13. filterDoctorByNameAndTime
    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(
            String name,
            String time) {

        List<Doctor> doctors =
                doctorRepository.findDoctorByNameContainsIgnoreCase(name);

        return filterDoctorByTime(doctors, time);
    }

    // 14. filterDoctorByNameAndSpecility
    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecility(
            String name,
            String specialty) {

        return doctorRepository
                .findDoctorByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty
                );
    }

    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecility(
            String time,
            String specialty) {

        List<Doctor> doctors =
                doctorRepository.findDoctorBySpecialtyIgnoreCase(specialty);

        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository
                .findDoctorBySpecialtyIgnoreCase(specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorsByTime(String time) {

        List<Doctor> doctors =
                doctorRepository.findAll();

        return filterDoctorByTime(doctors, time);
    }

    private boolean isTimeInPeriod(
            LocalTime time,
            String period) {

        if ("AM".equals(period)) {
            return time.isBefore(LocalTime.NOON);
        }

        if ("PM".equals(period)) {
            return !time.isBefore(LocalTime.NOON);
        }

        return false;
    }
// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.

// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.

// 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.


// 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.


// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.


// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).

// 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

   
}
