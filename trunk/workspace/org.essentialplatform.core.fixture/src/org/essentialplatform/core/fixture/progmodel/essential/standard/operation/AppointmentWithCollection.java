/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.standard.*;

import java.util.List;



@InDomain
public class AppointmentWithCollection {
	List<AppointmentWithCollection> otherAppointments;
	@TypeOf(AppointmentWithCollection.class)
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