package org.essentialplatform.domain;

import java.util.Date;
import org.essentialplatform.progmodel.standard.Value;

/**
 * An application-defined value object (datatype)
 * 
 * <p>
 * Note: not a nested class because otherwise the getCanonicalName() doesn't
 * match the actual name that EMF gives us (which has an embedded "$").
 */
@Value
public class EmfFacadeDatePeriod  {
	private Date from;
	private Date to;
	public Date getFrom() {
		return from;
	}
	public Date getTo() {
		return to;
	}
}