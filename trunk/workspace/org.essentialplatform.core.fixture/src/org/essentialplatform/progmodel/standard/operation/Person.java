/**
 * 
 */
package org.essentialplatform.progmodel.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public abstract class Person {
	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}