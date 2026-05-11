# Online-Course-Enrollment

Java implementation of an online course enrollment system, now with a JavaFX desktop frontend.

## Features
- Register students
- Add and list courses
- Enroll a student in a course
- Drop selected enrollments
- Persist students, courses, and enrollments in local text files

## Project Layout
- `Javasrc/src/model` - domain models
- `Javasrc/src/service` - business logic
- `Javasrc/src/repository` - file persistence
- `Javasrc/src/ui` - JavaFX application, controller, and views

## Run the JavaFX App
From `Javasrc/`:

```bash
mvn clean javafx:run
```

## Notes
- Data files are created in the same working directory (`students.txt`, `courses.txt`, `enrollments.txt`).
- Existing console entry point is still available in `Javasrc/src/Main.java`.
