package org.example.hogwarts.testrest;

import org.example.hogwarts.model.Faculty;
import org.example.hogwarts.model.Student;
import org.example.hogwarts.service.FacultyService;
import org.example.hogwarts.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration"
        }
)
@AutoConfigureTestRestTemplate
class FacultyControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createFaculty_returnsCreatedFaculty() {
        Faculty request = new Faculty();
        request.setName("Ravenclaw");
        request.setColor("Blue");

        Faculty saved = new Faculty();
        saved.setId(1L);
        saved.setName("Ravenclaw");
        saved.setColor("Blue");

        Mockito.when(facultyService.create(Mockito.any(Faculty.class))).thenReturn(saved);

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculties", request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
    }

    @Test
    void getFacultyById_returnsFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        Mockito.when(facultyService.getById(1L)).thenReturn(Optional.of(faculty));

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/1", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    @Test
    void getAllFaculties_returnsList() {
        Faculty f1 = new Faculty();
        f1.setId(1L);
        f1.setName("Hufflepuff");
        f1.setColor("Yellow");

        Faculty f2 = new Faculty();
        f2.setId(2L);
        f2.setName("Ravenclaw");
        f2.setColor("Blue");

        Mockito.when(facultyService.getAll()).thenReturn(List.of(f1, f2));

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculties",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void updateFaculty_returnsUpdatedFaculty() {
        Faculty request = new Faculty();
        request.setName("Gryffindor");
        request.setColor("Red");

        Faculty updated = new Faculty();
        updated.setId(1L);
        updated.setName("Gryffindor");
        updated.setColor("Red");

        Mockito.when(facultyService.update(Mockito.eq(1L), Mockito.any(Faculty.class))).thenReturn(updated);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculties/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    void deleteFaculty_returnsNoContent() {
        Mockito.when(facultyService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = restTemplate.exchange("/faculties/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void searchFacultyByNameOrColor_returnsList() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Mockito.when(facultyService.findByNameOrColorIgnoreCase("red")).thenReturn(List.of(faculty));

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculties/search?value=red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getStudentsOfFaculty_returnsList() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(15);

        Mockito.when(facultyService.getStudentsOfFaculty(1L)).thenReturn(List.of(student));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/faculties/1/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Harry");
    }
}

