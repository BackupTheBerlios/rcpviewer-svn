/**
 * 
 */
package org.essentialplatform.progmodel.standard.operation;
import org.essentialplatform.progmodel.standard.*;


@InDomain
public class AppointmentWithSingleReference {
	AppointmentWithSingleReference nextAppointment;
	public AppointmentWithSingleReference getNextAppointment() {
		return nextAppointment;
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