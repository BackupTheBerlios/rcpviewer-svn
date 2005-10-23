package de.berlios.rcpviewer.progmodel.extended;

import java.util.Date;
import de.berlios.rcpviewer.progmodel.extended.IClock;

/**
 * Implementation of {@link IClock} that simply returns the time according to 
 * the computer on which the clock is running.
 *  
 * @author Dan Haywood
 *
 */
public class PcClock implements IClock {

	public Date now() {
		return new Date();
	}

}
