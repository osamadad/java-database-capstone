# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

# Admin
1. Admin Login

Title:
As an admin, I want to log into the portal with my username and password, so that I can securely manage the platform.

Acceptance Criteria:

The admin can enter a username and password.
The system validates the provided credentials.
The admin is granted access to the admin portal when the credentials are valid.
The system rejects invalid credentials and displays an appropriate error message.
Unauthenticated users cannot access admin-only functionality.

Priority: High
Story Points: 3

Notes:

Admin authentication should be separate from patient/doctor access where applicable.
Passwords must not be stored as plain text.
2. Admin Logout

Title:
As an admin, I want to log out of the portal, so that I can protect system access.

Acceptance Criteria:

The admin can select the logout option.
The system terminates the admin's authenticated session.
The admin is redirected to the login page after logging out.
The admin cannot access protected pages using the previous session.

Priority: High
Story Points: 2

Notes:

Logout should invalidate/remove the relevant authentication mechanism.
3. Add Doctor

Title:
As an admin, I want to add doctors to the portal, so that doctors can provide appointments to patients.

Acceptance Criteria:

The admin can open the add-doctor form.
The admin can enter the doctor's required information.
The system validates the required fields.
The system prevents creation of a doctor with invalid or duplicate information where applicable.
A successfully added doctor appears in the doctor list.
The doctor can subsequently log into the portal using the appropriate credentials.

Priority: High
Story Points: 5

Notes:

Doctor information may include name, email, specialization, and contact information.
Define whether the admin creates the doctor's password or whether the doctor sets it separately.
4. Delete Doctor

Title:
As an admin, I want to delete a doctor's profile from the portal, so that doctors who are no longer available cannot receive new appointments.

Acceptance Criteria:

The admin can view the list of doctors.
The admin can select a doctor to delete.
The system asks for confirmation before deletion.
The doctor's profile is removed or deactivated after confirmation.
The deleted/deactivated doctor is no longer available for new bookings.
Existing appointments associated with the doctor are handled according to the system's defined business rules.

Priority: High
Story Points: 5

Notes:

Consider using soft deletion/deactivation instead of permanently deleting the doctor's database record.
Existing appointment history should generally be preserved.
5. View Monthly Appointment Statistics

Title:
As an admin, I want to run a stored procedure that returns the number of appointments per month, so that I can track platform usage statistics.

Acceptance Criteria:

A MySQL stored procedure exists for calculating appointment counts by month.
The procedure can be executed through the MySQL CLI.
The procedure returns the number of appointments grouped by month.
The results can be used by the admin to track appointment usage.
The procedure correctly handles months with no appointments.

Priority: Medium
Story Points: 3

Notes:

The stored procedure is executed through MySQL CLI rather than necessarily through the portal UI.
Define whether statistics should cover all historical appointments or a specified date range.
# Patient
6. View Doctors Without Logging In

Title:
As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering.

Acceptance Criteria:

An unauthenticated patient can access the doctor list.
The system displays available doctors.
The list includes relevant information such as the doctor's name and specialization.
Doctors who are unavailable for new bookings are not presented as available for booking.
Viewing the doctor list does not require an account.

Priority: High
Story Points: 3

Notes:

The information displayed publicly should not include sensitive doctor information.
7. Patient Sign Up

Title:
As a patient, I want to sign up using my email and password, so that I can book appointments.

Acceptance Criteria:

The patient can access the registration form.
The patient can provide an email and password.
The system validates the required registration fields.
The system prevents registration using an email that is already registered.
The password is stored securely.
A successfully registered patient can log into the portal.

Priority: High
Story Points: 5

Notes:

Email format and password requirements should be validated.
Additional patient information can be added if required by the system.
8. Patient Login

Title:
As a patient, I want to log into the portal, so that I can manage my bookings.

Acceptance Criteria:

The patient can enter their email and password.
The system validates the credentials.
The patient is granted access when the credentials are valid.
Invalid credentials result in an appropriate error message.
Unauthenticated users cannot access protected patient functionality.

Priority: High
Story Points: 3

Notes:

Authentication should identify the user as a patient and provide access only to patient functionality.
9. Patient Logout

Title:
As a patient, I want to log out of the portal, so that I can secure my account.

Acceptance Criteria:

The patient can select the logout option.
The system terminates the patient's authenticated session.
The patient is redirected to the login or public page.
The patient cannot access protected account information after logging out.

Priority: High
Story Points: 2

Notes:

The logout mechanism should invalidate the active authentication session/token as appropriate.
10. Book an Appointment

Title:
As a patient, I want to book an hour-long appointment with a doctor, so that I can consult with the doctor.

Acceptance Criteria:

The patient must be logged in to book an appointment.
The patient can select an available doctor.
The patient can select an available appointment slot.
Each appointment has a duration of one hour.
The system prevents booking an unavailable or already-booked slot.
The system confirms the appointment after successful booking.
The appointment appears in the patient's upcoming appointments.

Priority: High
Story Points: 8

Notes:

Doctor availability must be taken into account when displaying appointment slots.
The system should prevent double-booking.
11. View Upcoming Appointments

Title:
As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.

Acceptance Criteria:

The patient must be logged in to view their appointments.
The system displays the patient's upcoming appointments.
Each appointment displays relevant information such as doctor, date, time, and specialization.
Only appointments belonging to the logged-in patient are displayed.
Past appointments are not included in the upcoming appointments list.

Priority: High
Story Points: 3

Notes:

Appointment history could be implemented as a separate feature later.
# Doctor
12. Doctor Login

Title:
As a doctor, I want to log into the portal, so that I can manage my appointments.

Acceptance Criteria:

The doctor can enter their credentials.
The system validates the credentials.
The doctor is granted access when the credentials are valid.
Invalid credentials result in an appropriate error message.
The doctor can access doctor-specific functionality after logging in.

Priority: High
Story Points: 3

Notes:

Doctor access should be restricted to doctor-specific functionality.
13. Doctor Logout

Title:
As a doctor, I want to log out of the portal, so that I can protect my data.

Acceptance Criteria:

The doctor can select the logout option.
The system terminates the authenticated session.
The doctor is redirected to the login or public page.
The doctor cannot access protected doctor information after logging out.

Priority: High
Story Points: 2

Notes:

The logout mechanism should invalidate the active authentication session/token.
14. View Appointment Calendar

Title:
As a doctor, I want to view my appointment calendar, so that I can stay organized.

Acceptance Criteria:

The doctor must be logged in to access the calendar.
The calendar displays the doctor's scheduled appointments.
Appointments display relevant information such as patient, date, and time.
The calendar distinguishes available and booked time slots.
The doctor can navigate between different dates.

Priority: High
Story Points: 5

Notes:

The calendar should reflect the doctor's current availability.
Appointment times should be displayed according to the configured timezone.
15. Mark Unavailability

Title:
As a doctor, I want to mark myself as unavailable during specific time periods, so that patients can only book available slots.

Acceptance Criteria:

The doctor can select a date and time period during which they are unavailable.
The system prevents patients from booking appointments during the unavailable period.
The unavailable period appears on the doctor's calendar.
The system prevents the doctor from marking an already-booked appointment as unavailable.
The doctor can remove or modify an existing unavailability period where applicable.

Priority: High
Story Points: 5

Notes:

The system should validate that unavailable periods do not conflict with existing appointments.
Recurring unavailability could be added as a future feature.
16. Update Doctor Profile

Title:
As a doctor, I want to update my profile with my specialization and contact information, so that patients have up-to-date information.

Acceptance Criteria:

The doctor can access their profile.
The doctor can update their specialization.
The doctor can update their contact information.
The system validates the updated information.
The changes are saved successfully.
Updated information is displayed to patients where applicable.

Priority: Medium
Story Points: 3

Notes:

Define which profile fields the doctor is allowed to modify.
Some fields, such as the doctor's identity or account status, may need to remain admin-controlled.
17. View Patient Details

Title:
As a doctor, I want to view patient details for my upcoming appointments, so that I can be prepared for the consultation.

Acceptance Criteria:

The doctor must be logged in to access patient information.
The doctor can select an upcoming appointment.
The system displays the patient's relevant details.
The doctor can only access details for patients who have appointments with that doctor.
Sensitive patient information is not exposed to unauthorized users.

Priority: High
Story Points: 5

Notes:

Define exactly which patient details are required, such as name, contact information, and appointment information.
Patient privacy and authorization should be considered when implementing this feature.
