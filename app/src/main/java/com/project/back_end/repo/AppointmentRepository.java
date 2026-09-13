package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import jakarta.persistence.JoinTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {


    List<Appointment> findAppointmentByDoctor_IdAndAppointmentTimeBetween(Long doctorId, LocalDateTime appointmentTimeAfter, LocalDateTime appointmentTimeBefore);
// 2. Custom Query Methods:

    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availableTimes WHERE d.id = ?1 AND a.appointmentTime BETWEEN ?2 AND ?3")
    List<Appointment> getAppointmentByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE d.id = ?1 AND LOWER(p.name) LIKE LOWER(CONCAT('%', ?2, '%')) AND a.appointmentTime BETWEEN ?3 AND ?4")
    List<Appointment> getAppointmentsByDoctorIdAndPatientNameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId, String patientName, LocalDateTime appointmentTimeAfter, LocalDateTime appointmentTimeBefore);


    @Modifying
    @Transactional
    void deleteAllByDoctor_Id(Long doctorId);

    List<Appointment> findAppointmentByPatientId(Long patientId);

    List<Appointment> findAppointmentByPatientIdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', ?1, '%')) AND a.patient.id = ?2")
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', ?1, '%')) AND a.patient.id = ?2 and a.status=?3")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);

    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = ?1 WHERE a.id = ?2")
    void updateStatus(int status, long id);

    List<Appointment> findAppointmentByPatientIdAndStatus(Long patientId, int status);
}
