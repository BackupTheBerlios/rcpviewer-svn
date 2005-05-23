/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.operation;
import de.berlios.rcpviewer.progmodel.standard.*;


@InDomain
public class TestDomainClassOperationsAppointmentWithMutator {
	private String name;
	public void setName(String name) {
		this.name = name;
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