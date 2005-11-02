/**
 * 
 */
package org.essentialplatform.progmodel.standard.namingconventions;

import java.util.ArrayList;
import java.util.List;


public class Department {
	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void foo() { }
	public void bar() { }
	
	Employee supervisor;
	public Employee getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(Employee supervisor) {}
	
	List employees = new ArrayList();
	public List getEmployees() {
		return employees;
	}
	
	int numberOfEmployees;
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}
	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}
	
}