function selectRole(role) {
    setRole(role);

    if (role === "admin") {
        window.openModal("adminLogin");
    } else if (role === "doctor") {
        window.openModal("doctorLogin");
    } else if (role === "patient") {
        window.location.href = "/pages/patientDashboard.html";
    }
}


function renderContent() {
    const role = getRole();

    if (!role) {
        window.location.href = "/";
        return;
    }
}