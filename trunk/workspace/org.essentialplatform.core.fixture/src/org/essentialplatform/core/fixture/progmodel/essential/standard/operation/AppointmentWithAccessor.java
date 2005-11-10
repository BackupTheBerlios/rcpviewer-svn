/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;


@InDomain
public class AppointmentWithAccessor {
	String name;
	public String getName() {
		return name;
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