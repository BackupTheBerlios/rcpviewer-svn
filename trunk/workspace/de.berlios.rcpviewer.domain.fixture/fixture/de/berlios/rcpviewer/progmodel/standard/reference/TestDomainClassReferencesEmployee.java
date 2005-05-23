package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a m:1 bidirectional relationship with Employee.
 * 
 * <p>
 * TODO: DomainMarker is workaround
 */
@InDomain
public class TestDomainClassReferencesEmployee implements DomainMarker {
	public TestDomainClassReferencesEmployee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	TestDomainClassReferencesDepartment department;
	/**
	 * Should be picked up as representing a 1:1 reference to Department.
	 * @return
	 */
	public TestDomainClassReferencesDepartment getDepartment() {
		return department;
	}
	/**
	 * not public since not an operation.
	 * @param department
	 */
	void setDepartment(TestDomainClassReferencesDepartment department) {
		department.addToEmployees(this);
	}
	/**
	 * presence to indicate an optional reference.
	 *
	 */
	public void clearDepartment() {
		department.removeFromEmployees(this);
	}
	public boolean isTerminated() {
		return false;
	}
}