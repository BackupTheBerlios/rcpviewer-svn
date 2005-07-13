package de.berlios.rcpviewer.gui.acme.model;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class Customer {

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private int age;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public void birthday() {
		age++;
	}
	
}
