/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.namesanddesc;

import org.essentialplatform.progmodel.essential.app.Value;

@Value
public class TimePeriod {
	java.util.Date from;
	public java.util.Date getFrom() {
		return from;
	}
	public void setFrom(java.util.Date from) {
		this.from = from;
	}
	java.util.Date to;
	public java.util.Date getTo() {
		return to;
	}
	public void setTo(java.util.Date to) {
		this.to = to;
	}
}