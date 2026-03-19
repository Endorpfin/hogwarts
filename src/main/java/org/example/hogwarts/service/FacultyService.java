package org.example.hogwarts.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.hogwarts.model.Faculty;
import org.example.hogwarts.model.Student;
import org.example.hogwarts.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getById(Long id) {
        return facultyRepository.findById(id);
    }

    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findByNameOrColorIgnoreCase(String value) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(value, value);
    }

    public Faculty update(Long id, Faculty faculty) {
        if (!facultyRepository.existsById(id)) {
            return null;
        }
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public boolean delete(Long id) {
        if (!facultyRepository.existsById(id)) {
            return false;
        }
        facultyRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsOfFaculty(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found: " + facultyId));
        return faculty.getStudents();
    }
}

