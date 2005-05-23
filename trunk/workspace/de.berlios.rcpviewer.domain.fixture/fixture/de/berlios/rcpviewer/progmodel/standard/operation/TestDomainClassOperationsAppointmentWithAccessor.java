/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.operation;
import de.berlios.rcpviewer.progmodel.standard.*;


@InDomain
public class TestDomainClassOperationsAppointmentWithAccessor {
	String name;
	public String getName() {
		return name;
	}
	public void moveTo(
			TestDomainClassOperationsTimePeriod newPeriod, 
			String agenda) {
	}
	public static void createAt(
			TestDomainClassOperationsTimePeriod timePeriod,
			String rationale) {
	}
}