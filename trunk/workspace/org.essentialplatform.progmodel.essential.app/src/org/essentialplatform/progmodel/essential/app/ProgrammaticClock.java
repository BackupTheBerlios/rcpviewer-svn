package org.essentialplatform.progmodel.essential.app;

import java.util.Date;

import org.essentialplatform.progmodel.essential.app.IClock;

/**
 * Implementation of {@link IClock} that simply returns the time that is set.
 * 
 * <p>
 * If no time has been set, then will return null. 
 * 
 * @author Dan Haywood
 */
public class ProgrammaticClock implements IClock {

	private Date _now;
	
	public ProgrammaticClock() {
	}

	public Date now() {
		return _now;
	}

	/**
	 * Sets the time (to be returned by {@link #now()}.
	 * 
	 * @param now
	 */
	public void setTime(Date time) {
		_now = time;
	}
	

}
