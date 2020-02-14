package br.com.devdojo.javaclient;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;

public class JavaSpringClientTest {
	public static void main(String[] args) {
		
		
//		System.out.println("STUDENT-GET-FOR-OBJECT: " + studentGetForObject);
//		System.out.println("STUDENT-GET-FOR-ENTITY: " + studentGetForEntity.getBody());
//		System.out.println("STUDENT-GET-ALL-EXCHANGE-WITH-PAGING: " + exchangeGetAll.getBody().getContent());
		
// THE BELOW CODE DOESN'T WORK WHEN PAGING BEING USED IN GET (ALL) REQUEST
//		Student[] students = restTemplate.getForObject("/", Student[].class);
//		System.out.println(Arrays.toString(students));
//		ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null, 
//				new ParameterizedTypeReference<List<Student>>() {});
//		System.out.println("STUDENT-ALL-EXCHANGE-WITHOUT-PAGING: " + exchange.getBody());
				
//		System.out.println("STUDENT-POST-FOR-OBJECT: " + studentPostForObject);
//		System.out.println("STUDENT-POST-FOR-ENTITY: " + studentPostForEntity);
//		System.out.println("STUDENT-POST-EXCHANGE: " + exchangePost);

		/**************/
		/**************/
		/**************/
				
		Student studentPost = new Student();
		studentPost.setName("Bleno");
		studentPost.setEmail("bleno@bleno.com");
		studentPost.setId(40L);
		
		JavaClientDAO dao = new JavaClientDAO();
//		System.out.println(dao.listAll());
//		System.out.println(dao.findById(8));
//		System.out.println(dao.save(studentPost));
//		dao.update(studentPost);
		dao.delete(36);
	}
}
