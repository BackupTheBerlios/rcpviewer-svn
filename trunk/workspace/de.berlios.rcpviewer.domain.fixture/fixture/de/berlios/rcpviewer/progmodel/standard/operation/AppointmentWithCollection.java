/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.operation;
import de.berlios.rcpviewer.progmodel.standard.*;

import java.util.List;



@InDomain
public class AppointmentWithCollection {
	List<AppointmentWithCollection> otherAppointments;
	@Associates(AppointmentWithCollection.class)
	public List<AppointmentWithCollection> getOtherAppointments() {
		return otherAppointments;
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