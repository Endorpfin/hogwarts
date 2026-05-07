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
class StudentControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void createStudent_returnsCreatedStudent() {
        Student request = new Student();
        request.setName("Harry");
        request.setAge(15);

        Student saved = new Student();
        saved.setId(1L);
        saved.setName("Harry");
        saved.setAge(15);

        Mockito.when(studentService.create(Mockito.any(Student.class))).thenReturn(saved);

        ResponseEntity<Student> response = restTemplate.postForEntity("/students", request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Harry");
    }

    @Test
    void getStudentById_returnsStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione");
        student.setAge(14);
        Mockito.when(studentService.getById(1L)).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = restTemplate.getForEntity("/students/1", Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hermione");
    }

    @Test
    void getAllStudents_returnsList() {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("Harry");
        s1.setAge(15);

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("Ron");
        s2.setAge(15);

        Mockito.when(studentService.getAll()).thenReturn(List.of(s1, s2));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/students",
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
    void updateStudent_returnsUpdatedStudent() {
        Student request = new Student();
        request.setName("Luna");
        request.setAge(16);

        Student updated = new Student();
        updated.setId(1L);
        updated.setName("Luna");
        updated.setAge(16);

        Mockito.when(studentService.update(Mockito.eq(1L), Mockito.any(Student.class))).thenReturn(updated);

        ResponseEntity<Student> response = restTemplate.exchange(
                "/students/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Luna");
    }

    @Test
    void deleteStudent_returnsNoContent() {
        Mockito.when(studentService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = restTemplate.exchange("/students/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getStudentsByAgeBetween_returnsFilteredList() {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("Harry");
        s1.setAge(15);

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("Draco");
        s2.setAge(16);

        Mockito.when(studentService.findByAgeBetween(14, 16)).thenReturn(List.of(s1, s2));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/students/age?min=14&max=16",
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
    void getFacultyOfStudent_returnsFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(10L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Mockito.when(studentService.getFacultyOfStudent(1L)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/students/1/faculty", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }
}

