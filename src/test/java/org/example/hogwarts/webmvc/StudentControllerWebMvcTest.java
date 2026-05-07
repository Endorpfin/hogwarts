package org.example.hogwarts.webmvc;

import org.example.hogwarts.controller.StudentController;
import org.example.hogwarts.model.Faculty;
import org.example.hogwarts.model.Student;
import org.example.hogwarts.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudent_returnsOk() throws Exception {
        Student request = new Student();
        request.setName("Harry");
        request.setAge(15);

        Student saved = new Student();
        saved.setId(1L);
        saved.setName("Harry");
        saved.setAge(15);

        Mockito.when(studentService.create(Mockito.any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry"));
    }

    @Test
    void getStudentById_returnsOk() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione");
        student.setAge(14);

        Mockito.when(studentService.getById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hermione"));
    }

    @Test
    void getAllStudents_returnsOk() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ron");
        student.setAge(15);

        Mockito.when(studentService.getAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ron"));
    }

    @Test
    void updateStudent_returnsOk() throws Exception {
        Student request = new Student();
        request.setName("Luna");
        request.setAge(16);

        Student updated = new Student();
        updated.setId(1L);
        updated.setName("Luna");
        updated.setAge(16);

        Mockito.when(studentService.update(Mockito.eq(1L), Mockito.any(Student.class))).thenReturn(updated);

        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"));
    }

    @Test
    void deleteStudent_returnsNoContent() throws Exception {
        Mockito.when(studentService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudentsByAgeBetween_returnsOk() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(15);

        Mockito.when(studentService.findByAgeBetween(10, 20)).thenReturn(List.of(student));

        mockMvc.perform(get("/students/age")
                        .param("min", "10")
                        .param("max", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry"));
    }

    @Test
    void getFacultyOfStudent_returnsOk() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(10L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Mockito.when(studentService.getFacultyOfStudent(1L)).thenReturn(faculty);

        mockMvc.perform(get("/students/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }
}

