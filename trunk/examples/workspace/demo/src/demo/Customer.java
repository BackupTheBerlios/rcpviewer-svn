package demo;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@Lifecycle(instantiable=true)
@InDomain
public class Customer {

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void changeName(
			@Named("Last name")
			final String lastName) {
		this.lastName = lastName;
	}
	public String toString() {
		return firstName==null?"(set first name)": firstName;
	}

}
