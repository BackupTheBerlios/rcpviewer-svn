/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithUnsettableAttribute {
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