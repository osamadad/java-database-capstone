# Section 1: Architecture summary

This application uses Spring MVC with Thymeleaf for the web-based admin/doctor dashboard, and a REST API for other models using JSON as the user interface layer. Controllers will route both requests to the same service layer, which uses the appropriate repository from a MySQL database for relational patient, doctor, appointment, and admin data, and MongoDB that contains prescriptions.

# Section 2: Numbered flow of data and control

1. User interface layer: Thymeleaf-based web dashboards for admin/doctor, and a REST API client for appointments.
2. Controller layer: Both user interfaces use controllers that handle actions/requests.
3. Service layer: Controllers call the service for business logic.
4. Repository layer: 2 repositories based on MySQL and MongoDB for data access operations on them.
5. Database access: The repositories access their database, where SQL holds normalized relational data, and MongoDB holds flexible nested data structures.
6. Model binding: In MySQL, data in cenverted into JPA entities using @Entity, and MongoDB loaded into a document object using @Document.
7. Application model in use: For MVC model are rendered as HTML, while REST is serialized into JSON.
