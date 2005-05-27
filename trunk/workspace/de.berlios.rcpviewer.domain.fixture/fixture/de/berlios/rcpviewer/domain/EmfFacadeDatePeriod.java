package de.berlios.rcpviewer.domain;

import java.util.Date;
import de.berlios.rcpviewer.progmodel.standard.Value;

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
}