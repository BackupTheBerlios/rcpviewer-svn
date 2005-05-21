/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesUnsettabilityCustomerWithUnsettableAttribute {
	int age;
	public boolean isUnsetAge() {
		return age == -1;
	}
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