package br.com.devdojo;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {
	@Autowired
	private StudentRepository studentRepository;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createShouldPersistData() {
		Student student = new Student("Rodrigo", "rodrigo@devdojo.com.br");
		this.studentRepository.save(student);

		Assertions.assertThat(student.getId()).isNotNull();
		Assertions.assertThat(student.getName()).isEqualTo("Rodrigo");
		Assertions.assertThat(student.getEmail()).isEqualTo("rodrigo@devdojo.com.br");
	}

	@Test
	public void deleteShouldRemoveData() {
		Student student = new Student("Rodrigo", "rodrigo@devdojo.com.br");
		this.studentRepository.save(student);
		studentRepository.delete(student);
		
		//IF STUDENT IS NOT PRESENT AFTER BEING DELETED: THE TEST RETURNS TRUE (SUCCESS)
		Assertions.assertThat(!studentRepository.findById(student.getId()).isPresent()).isTrue();
	}
	
	@Test
	public void updateShouldChangeAndPersistData() {
		Student student = new Student("Rodrigo", "rodrigo@devdojo.com.br");		
		this.studentRepository.save(student);
		student.setName("Donizetti");
		student.setEmail("donizetti@devdojo.com.br");
		//UPDATES THE STUDENT, BECAUSE THE OBJECT ALREADY HAVE A VALID ID IN THE DB
		this.studentRepository.save(student);
		student = this.studentRepository.findById(student.getId()).get();
		
		Assertions.assertThat(student.getName()).isEqualTo("Donizetti");
		Assertions.assertThat(student.getEmail()).isEqualTo("donizetti@devdojo.com.br");
	}
	
	@Test
	public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
		Student student1 = new Student("Rodrigo", "rodrigo@devdojo.com.br");		
		Student student2 = new Student("rodrigo", "rodrigo123@devdojo.com.br");		
		this.studentRepository.save(student1);
		this.studentRepository.save(student2);
		List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("rodrigo");
		
		Assertions.assertThat(studentList.size()).isEqualTo(2);
	}
	
	@Test
	public void createWhenNameIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("NAME field cannot be empty!");
		this.studentRepository.save(new Student());
	}
	
	@Test
	public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("EMAIL field cannot be empty!");
		Student student = new Student();
		student.setName("rodrigo");
		this.studentRepository.save(student);
	}
	
	@Test
	public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		Student student = new Student();
		student.setName("rodrigo");
		student.setEmail("rodrigo"); //INVALID-EMAIL
		this.studentRepository.save(student);
	}
}
