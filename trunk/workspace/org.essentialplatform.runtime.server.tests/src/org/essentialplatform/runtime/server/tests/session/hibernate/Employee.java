package org.essentialplatform.runtime.server.tests.session.hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import org.essentialplatform.progmodel.essential.app.InDomain;

@Entity
@InDomain
public class Employee {
	
	
	private Integer id;
	@Id(generate=GeneratorType.AUTO)
	@org.essentialplatform.progmodel.essential.app.Id(1)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private String surname;
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	
	private Department department;
	@javax.persistence.ManyToOne(cascade={CascadeType.ALL})
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public void associateDepartment(Department department) {
		setDepartment(department);
	}
	public void dissociateDepartment(Department department) {
		setDepartment(null);
	}
	
	
	
	/** used as title */
	public String toString() { return getFirstName() + " " + getSurname(); }

}
