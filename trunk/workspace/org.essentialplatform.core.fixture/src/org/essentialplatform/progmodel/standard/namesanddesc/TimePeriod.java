/**
 * 
 */
package org.essentialplatform.progmodel.standard.namesanddesc;

import org.essentialplatform.progmodel.standard.Value;

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