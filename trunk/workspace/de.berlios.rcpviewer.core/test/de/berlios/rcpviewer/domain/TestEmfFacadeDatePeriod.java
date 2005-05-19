package de.berlios.rcpviewer.domain;

import java.util.Date;
import de.berlios.rcpviewer.progmodel.standard.Value;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

/**
 * An application-defined value object (datatype)
 * 
 * Note: not a nested class because otherwise the getCanonicalName() doesn't
 * match the actual name that EMF gives us (which has an embedded "$").
 * 
 * TODO: should be enough just to be annotated with @Value.
 */
@Value
public class TestEmfFacadeDatePeriod implements ValueMarker {
	private Date from;
	private Date to;
}