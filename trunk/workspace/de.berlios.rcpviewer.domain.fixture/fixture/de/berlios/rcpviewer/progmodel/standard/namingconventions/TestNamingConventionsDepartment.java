/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.namingconventions;

import java.util.ArrayList;
import java.util.List;


public class TestNamingConventionsDepartment {
	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void foo() { }
	public void bar() { }
	
	TestNamingConventionsEmployee supervisor;
	public TestNamingConventionsEmployee getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(TestNamingConventionsEmployee supervisor) {}
	
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