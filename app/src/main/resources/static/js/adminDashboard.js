/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
import { openModal } from "./components/modals.js";
import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";


/*
 * Load all doctors and display their cards
 */
async function loadDoctorCards() {

    const contentDiv = document.getElementById("content");

    if (!contentDiv) {
        return;
    }

    try {

        const doctors = await getDoctors();

        contentDiv.innerHTML = "";

        renderDoctorCards(doctors);

    } catch (error) {

        console.error("Error loading doctors:", error);

        contentDiv.innerHTML = "<p>No doctors found.</p>";
    }
}


/*
 * Render doctor cards
 */
function renderDoctorCards(doctors) {

    const contentDiv = document.getElementById("content");

    if (!contentDiv) {
        return;
    }

    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {

        contentDiv.innerHTML = "<p>No doctors found</p>";

        return;
    }


    doctors.forEach((doctor) => {

        const card = createDoctorCard(doctor);

        contentDiv.appendChild(card);

    });
}


/*
 * Search and filter doctors
 */
async function filterDoctorsOnChange() {

    const searchBar = document.getElementById("searchBar");
    const timeFilter = document.getElementById("timeFilter");
    const specialtyFilter = document.getElementById("specialtyFilter");

    const name = searchBar ? searchBar.value : "";
    const time = timeFilter ? timeFilter.value : "";
    const specialty = specialtyFilter ? specialtyFilter.value : "";


    try {

        const doctors = await filterDoctors(
            name,
            time,
            specialty
        );

        renderDoctorCards(doctors);

    } catch (error) {

        console.error("Error filtering doctors:", error);

        const contentDiv = document.getElementById("content");

        if (contentDiv) {
            contentDiv.innerHTML = "<p>No doctors found</p>";
        }
    }
}


/*
 * Add a new doctor
 */
window.adminAddDoctor = async function () {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Session expired or invalid login.");
        return;
    }


    const availability = [];

    const availabilityCheckboxes =
        document.querySelectorAll(
            '#addDoctorForm input[type="checkbox"]:checked'
        );


    availabilityCheckboxes.forEach((checkbox) => {
        availability.push(checkbox.value);
    });


    const doctor = {
        name: document.getElementById("doctorName")?.value,
        specialization:
            document.getElementById("doctorSpecialization")?.value,
        email: document.getElementById("doctorEmail")?.value,
        password: document.getElementById("doctorPassword")?.value,
        mobileNo:
            document.getElementById("doctorMobileNo")?.value,
        availability: availability
    };


    try {

        const result = await saveDoctor(
            doctor,
            token
        );


        if (result.success) {

            alert(result.message);

            const modal = document.getElementById("modal");

            if (modal) {
                modal.style.display = "none";
            }

            await loadDoctorCards();

        } else {

            alert(result.message);
        }

    } catch (error) {

        console.error("Error adding doctor:", error);

        alert("Failed to add doctor.");
    }
};


/*
 * Setup event listeners after the page is loaded
 */
document.addEventListener("DOMContentLoaded", () => {

    const addDocBtn =
        document.getElementById("addDocBtn");

    if (addDocBtn) {

        addDocBtn.addEventListener("click", () => {
            openModal("addDoctor");
        });

    }


    const searchBar =
        document.getElementById("searchBar");

    if (searchBar) {

        searchBar.addEventListener(
            "input",
            filterDoctorsOnChange
        );

    }


    const timeFilter =
        document.getElementById("timeFilter");

    if (timeFilter) {

        timeFilter.addEventListener(
            "change",
            filterDoctorsOnChange
        );

    }


    const specialtyFilter =
        document.getElementById("specialtyFilter");

    if (specialtyFilter) {

        specialtyFilter.addEventListener(
            "change",
            filterDoctorsOnChange
        );

    }


    loadDoctorCards();
});