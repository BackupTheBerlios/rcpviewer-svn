package org.essentialplatform.progmodel.standard.reference;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.standard.*;

/**
 * The @OppositeOf annotation is for these fixture classes applied to the
 * "parent" end. 
 */
@InDomain
public class BiDir1Employee {

	BiDir1Department department;
	/**
	 * Should be picked up as representing a 1:1 reference to 
	 * BiDirDepartment.
	 * 
	 * @return
	 */
	public BiDir1Department getDepartment() {
		return department;
	}
	/**
	 * not public since not an operation.
	 * @param department
	 */
	void setDepartment(BiDir1Department department) {
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