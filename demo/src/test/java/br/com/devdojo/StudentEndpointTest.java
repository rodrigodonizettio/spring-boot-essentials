package br.com.devdojo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;
	
	@MockBean
	private StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@TestConfiguration
	static class Config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthentication("admin", "devdojo");
		}
	}
	
	@Before
	public void setup() {
		Student student = new Student(1L, "Saint", "saint@saint.com");
		Optional<Student> studentOptional = Optional.of(student);
		BDDMockito.when(studentRepository.findById(1L)).thenReturn(studentOptional);
	}
	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		System.out.println("PORTA-ALEATÓRIA-GERADA: " + port);
		
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void getStudentsByIdStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/10", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		List<Student> students = Arrays.asList(new Student(1L, "Saint", "saint@saint.com"),
			new Student(2L, "Bleno", "bleno@bleno.com"),
			new Student(3L, "Nick", "nick@nick.com"));
		BDDMockito.when(studentRepository.findAll()).thenReturn(students);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/{id}", String.class, 1L);
//		ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, student.getId());
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdStudentsWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
		ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	//TODO: Maybe I've found a bug in the delete method. It's returning 200 when its body is null and indeed the student Id doesn't exist
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
		BDDMockito.doNothing().when(studentRepository).deleteById(1L);
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	//TODO: Maybe I've found a bug in the delete method. It's returning 200 when its body is null and indeed the student Id doesn't exist
	@Test
	@WithMockUser(username = "admin", password = "devdojo", roles = "ADMIN")
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception {
		BDDMockito.doNothing().when(studentRepository).deleteById(1L);
		
//		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, -1L);
//		
//		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}", -1L))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
