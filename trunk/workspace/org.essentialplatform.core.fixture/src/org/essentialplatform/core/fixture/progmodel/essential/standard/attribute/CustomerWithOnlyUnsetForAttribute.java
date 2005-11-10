/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

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