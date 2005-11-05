/**
 * 
 */
package org.essentialplatform.progmodel.standard.namesanddesc;
import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.standard.*;


@InDomain
public class Appointment {
	public void moveTo(
			@Named("newPeriod")
			@DescribedAs("The time when the appointment should now be scheduled")
			TimePeriod newPeriod, 
			@Named("rationale")
			@DescribedAs("The reasoning for moving the appointment")
			String rationale) {
	}
	public static void createAt(
			@Named("timePeriod")
			@DescribedAs("When the appointment is to run to and from")
			TimePeriod timePeriod,
			@Named("agenda")
			@DescribedAs("The agenda for this appointment")
			String agenda) {
	}
}