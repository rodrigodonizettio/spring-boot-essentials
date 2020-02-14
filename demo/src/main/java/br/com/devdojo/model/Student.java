package br.com.devdojo.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	public Student() { }
	
	public Student(
			@NotEmpty(message = "NAME field cannot be empty!") String name,
			@NotEmpty(message = "EMAIL field cannot be empty!") @Email String email) {
		super();
		this.name = name;
		this.email = email;
	}
	
	public Student(			
			@NotEmpty(message = "ID field cannot be empty!") Long id,
			@NotEmpty(message = "NAME field cannot be empty!") String name,
			@NotEmpty(message = "EMAIL field cannot be empty!") @Email String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	

	@NotEmpty(message="NAME field cannot be empty!")
	private String name;
	
	@NotEmpty(message="EMAIL field cannot be empty!")
	@Email
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", email=" + email + "]";
	}
}
