/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithOnlyUnsetForAttribute {
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