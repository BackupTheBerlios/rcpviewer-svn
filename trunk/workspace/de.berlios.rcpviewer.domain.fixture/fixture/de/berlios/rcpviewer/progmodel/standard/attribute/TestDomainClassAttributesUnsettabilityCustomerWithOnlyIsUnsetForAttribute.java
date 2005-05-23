/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class TestDomainClassAttributesUnsettabilityCustomerWithOnlyIsUnsetForAttribute {
	int age;
	/**
	 * Not having unsetXxx means this attribute isn't unsettable.
	 * @return
	 */
	public boolean isUnsetAge() {
		return age == -1;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}