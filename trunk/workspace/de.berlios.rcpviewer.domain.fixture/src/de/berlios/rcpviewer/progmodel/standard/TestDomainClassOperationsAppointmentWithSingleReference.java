/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;


@InDomain
public class TestDomainClassOperationsAppointmentWithSingleReference {
	TestDomainClassOperationsAppointmentWithSingleReference nextAppointment;
	public TestDomainClassOperationsAppointmentWithSingleReference getNextAppointment() {
		return nextAppointment;
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