/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesUnsettabilityCustomerWithOnlyUnsetForAttribute {
	int age;
	/**
	 * Not having isUnsetXxx means this attribute isn't unsettable.
	 * @return
	 */
	public void unsetAge() {
		age = -1;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}