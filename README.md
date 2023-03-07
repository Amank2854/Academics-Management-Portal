# CS305 - Software Engineering Project

**Aman Kumar <br>
2020CSB1153 <br>**

This is a multi-user database application for
managing the academics of an academic institute. <br>

our academic ecosystem for UG programs basically
consists of the following stakeholders. <br><br>
● Students <br>
● Faculty <br>
● Academics Office <br>

### Pre-requisite: <br>

* JDK 18.0.2 (preferred)
* Postgresql

### How to run: <br>

Run the Main.java file <br>
For generating Test Report run <br> `gradle build jacocotestreport` <br>
To run <br>
`gradle run`


### Documents along with code:
* Unit test plan
* JaCoCo code coverage report
* UML Diagram (Class Diagram)

### Assumptions:

* Some data has been preloaded into the Database.
* For graduation a student must have completed 60 core credits and 30 elective credits.
* Only Program Core and Program Electives are present.

### Functionalities:

* Student can Enroll or Drop courses, view grades, view their CGPA, update their profile, and check their graduation status.
* Faculty can Offer a course, specify its prerequisite and CG criteria and Offered branch, remove an offered course, view grades of all students of a course, upload course grades and update their profile.
* Academic Office can add courses into course catalog, view grades of all students of a course, Generate transcript of a student, update the semester and check the graduation status of a student.
* Users have to choose their user type before login and then provide their email and password to get the functionalities they can perform.