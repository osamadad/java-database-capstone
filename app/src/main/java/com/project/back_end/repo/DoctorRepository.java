package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    Doctor findDoctorByEmail(String email);

    List<Doctor> findDoctorByNameContainsIgnoreCase(String name);

    List<Doctor> findDoctorByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    List<Doctor> findDoctorBySpecialtyIgnoreCase(String specialty);

}