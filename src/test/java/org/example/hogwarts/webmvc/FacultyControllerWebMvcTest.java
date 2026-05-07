package org.example.hogwarts.webmvc;

import org.example.hogwarts.controller.FacultyController;
import org.example.hogwarts.model.Faculty;
import org.example.hogwarts.model.Student;
import org.example.hogwarts.service.FacultyService;
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

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void createFaculty_returnsOk() throws Exception {
        Faculty request = new Faculty();
        request.setName("Ravenclaw");
        request.setColor("Blue");

        Faculty saved = new Faculty();
        saved.setId(1L);
        saved.setName("Ravenclaw");
        saved.setColor("Blue");

        Mockito.when(facultyService.create(Mockito.any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ravenclaw"));
    }

    @Test
    void getFacultyById_returnsOk() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        Mockito.when(facultyService.getById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slytherin"));
    }

    @Test
    void getAllFaculties_returnsOk() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");

        Mockito.when(facultyService.getAll()).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hufflepuff"));
    }

    @Test
    void updateFaculty_returnsOk() throws Exception {
        Faculty request = new Faculty();
        request.setName("Gryffindor");
        request.setColor("Red");

        Faculty updated = new Faculty();
        updated.setId(1L);
        updated.setName("Gryffindor");
        updated.setColor("Red");

        Mockito.when(facultyService.update(Mockito.eq(1L), Mockito.any(Faculty.class))).thenReturn(updated);

        mockMvc.perform(put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void deleteFaculty_returnsNoContent() throws Exception {
        Mockito.when(facultyService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchFacultyByNameOrColor_returnsOk() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Mockito.when(facultyService.findByNameOrColorIgnoreCase("red")).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculties/search").param("value", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    @Test
    void getStudentsOfFaculty_returnsOk() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(15);

        Mockito.when(facultyService.getStudentsOfFaculty(1L)).thenReturn(List.of(student));

        mockMvc.perform(get("/faculties/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry"));
    }
}

