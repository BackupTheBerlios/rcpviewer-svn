/**
 * 
 */
package org.essentialplatform.progmodel.standard.operation;
import org.essentialplatform.progmodel.standard.*;


@InDomain
public class AppointmentWithMutator {
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public void moveTo(
			TimePeriod newPeriod, 
			String agenda) {
	}
	public static void createAt(
			TimePeriod timePeriod,
			String rationale) {
	}
}