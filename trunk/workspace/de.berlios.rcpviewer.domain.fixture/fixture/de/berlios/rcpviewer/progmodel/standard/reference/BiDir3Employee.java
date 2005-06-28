package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * <p>
 * The @OppositeOf annotation is for these fixture classes applied to both ends. 
 */
@InDomain
public class BiDir3Employee {

	BiDir3Department department;
	/**
	 * Should be picked up as representing a 1:1 reference to 
	 * BiDirDepartment.
	 * 
	 * @return
	 */
	@OppositeOf("employees")
	public BiDir3Department getDepartment() {
		return department;
	}
	/**
	 * not public since not an operation.
	 * @param department
	 */
	void setDepartment(BiDir3Department department) {
		department.addToEmployees(this);
	}
}