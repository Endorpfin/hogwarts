--liquibase formatted sql

--changeset student:index-student-name
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);

--changeset faculty:index-faculty-name-color
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);
