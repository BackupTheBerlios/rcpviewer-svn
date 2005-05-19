/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

@InDomain
public class TestDomainClassOperationsTimePeriod implements ValueMarker {
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