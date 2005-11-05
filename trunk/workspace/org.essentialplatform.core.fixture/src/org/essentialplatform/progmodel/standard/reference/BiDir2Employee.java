package org.essentialplatform.progmodel.standard.reference;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.OppositeOf;
import org.essentialplatform.progmodel.standard.*;

/**
 * The @OppositeOf annotation is for these fixture classes applied to the
 * "child" end. 
 */
@InDomain
public class BiDir2Employee {

	BiDir2Department department;
	/**
	 * Should be picked up as representing a 1:1 reference to 
	 * BiDirDepartment.
	 * 
	 * @return
	 */
	@OppositeOf("employees")
	public BiDir2Department getDepartment() {
		return department;
	}
	/**
	 * not public since not an operation.
	 * @param department
	 */
	void setDepartment(BiDir2Department department) {
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