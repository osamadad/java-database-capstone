package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    @GetMapping("/adminDashboard")
    public String adminDashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/doctorDashboard")
    public String doctorDashboard() {
        return "doctor/doctorDashboard";
    }
}