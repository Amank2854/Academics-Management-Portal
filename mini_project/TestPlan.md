# Unit Test Plan

### Introduction
The Test Plan is designed to prescribe the scope and approach of this project. The plan identifies the items to be tested, the features to be tested, the types of testing to be performed, and schedule required to complete testing, and the risks associated with the plan.

### Features to be tested
This includes the functionality requirements of the project. <br>

* Main 
  * Login Feature: Each user can choose their user type and can log in into their respective interface.
  * Logout Feature.

* Student
  * Add/Drop Course: A student can add or drop from the list of courses presented to them. They can only register if they clear the prerequisites and have the required CGPA(if any CGPA requirement is present). They can only drop courses for which they have enrolled.
  * View Grades: A student can view the grades of all the courses for which they have registered and completed the course.
  * Show CGPA: Student can view their overall CGPA.
  * Update Profile: A student can update their Name, contact, batch and password if they want to do so.
  * Check Graduation Status: A student can check if they have completed all the required credits necessary to graduate from the college.
  * Logout: Logout from the interface.

* Faculty
  * Register/Deregister Course: A faculty can select a course to be offered to students from the course catalog. They can also keep a CGPA criteria. They can remove courses from the offerings if they want.
  * View Grades: They can view grades of all the students enrolled in a given course.
  * Upload Grades: They can upload grades of the students for a course.
  * Update Profile: A faculty can update their Name, Department, Contact, join date and Password if they want.
  * Logout: Logout from the interface.

* Acad Office
  * Edit Course Catalog: Acad office can add a course into the catalog from where Faculty can offer courses.
  * View Grades: Acad office can view grades of all the students from a given course.
  * Generate Transcript: Can Generate transcript of a given student.
  * Update Semester: Can update the current semester.
  * Check Graduation status: Can check graduation status of a given student.
  * Logout: Logout from the interface.

### Approach
**Unit Testing:** Testing each method of each class individually and using JUnit Framework for Java Testing.

### Test Deliverables
* **Test Plan**: The current document.
* **Unit Test**: Using the unit testing approach to test each method of the project.
* **JaCoCo Report**: Using the Java Code Coverage Report to report the overall coverage of the code using the test cases used in Unit test.

### Testing Tasks
* Test environment should be ready prior to test execution phase.
* JaCoCo report needs to be prepared.
* Quality Expectations must be met.
* Ensuring that maximum number of test cases are passed.

### Environmental Needs

| No. | Resources  | Description                                              |
|-----|------------|----------------------------------------------------------|
| 1.  | JDK        | a Java Development Kit to support Java Environment       |
| 2.  | Database   | Postgresql to support SQL queries and to create database |
| 3.  | Build Tool | To automate creating of Java Application using Gradle    |
| 4.  | JaCoCo     | To generate code coverage Report                         |
| 5.  | JUnit      | To perform unit testing on methods                       |

### Risks

Releasing final production without testing using tests which cover all cases may lead to problems, bugs and inefficient application. Hence, we should use tests which cover all the corner cases so that we do not encounter bugs in the final production.