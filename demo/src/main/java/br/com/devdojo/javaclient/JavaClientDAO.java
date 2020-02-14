package br.com.devdojo.javaclient;

import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.devdojo.handler.RestResponseExceptionHandler;
import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;

public class JavaClientDAO {
	/***
	 * REST-TEMPLATE - GET-ALL AND GET-BYID
	 */
	private RestTemplate restTemplateGet = new RestTemplateBuilder()
		.rootUri("http://localhost:8080/v1/protected/students")
		.basicAuthentication("user", "devdojo")
		.errorHandler(new RestResponseExceptionHandler())
		.build();
	
	/***
	 * REST-TEMPLATE - POST
	 */
	private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
			.rootUri("http://localhost:8080/v1/admin/students")
			.basicAuthentication("admin", "devdojo")
			.errorHandler(new RestResponseExceptionHandler())
			.build();
	
	public Student findById(long id) {
		return restTemplateGet.getForObject("/{id}", Student.class, id);
//		ResponseEntity<Student> studentGetForEntity = restTemplateGet.getForEntity("/{id}", Student.class, 8);
	}
	
	public List<Student> listAll() {
		ResponseEntity<PageableResponse<Student>> exchangeGetAll = 
			restTemplateGet.exchange("/", HttpMethod.GET, null, 
			new ParameterizedTypeReference<PageableResponse<Student>>() {});
		return exchangeGetAll.getBody().getContent();
	}
	
	public Student save(Student student) {
		ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange("/",
			HttpMethod.POST,
			new HttpEntity<>(student, createJSONHeader()),
			Student.class);
		return exchangePost.getBody();
	}
	
	public void update(Student student) {
		restTemplateAdmin.put("/", student);
	}
	
	public void delete(long id) {
		restTemplateAdmin.delete("/{id}", id);
	}
	
	private static HttpHeaders createJSONHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
