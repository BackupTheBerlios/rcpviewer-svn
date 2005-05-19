/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

import java.util.List;


@InDomain
public class TestDomainClassOperationsAppointmentWithCollection {
	List<TestDomainClassOperationsAppointmentWithCollection> otherAppointments;
	@Associates(TestDomainClassOperationsAppointmentWithCollection.class)
	public List<TestDomainClassOperationsAppointmentWithCollection> getOtherAppointments() {
		return otherAppointments;
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