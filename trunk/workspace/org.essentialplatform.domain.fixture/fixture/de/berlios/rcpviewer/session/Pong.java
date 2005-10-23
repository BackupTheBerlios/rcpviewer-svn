package de.berlios.rcpviewer.session;

import static de.berlios.rcpviewer.progmodel.extended.IPrerequisites.Constraint.INVISIBLE;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.require;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * This class is not observed, but has state that affects the prerequisites of
 * the {@link de.berlios.rcpviewer.session.Ping} that references it.
 * 
 * @author Dan Haywood
 *
 */
@InDomain
public class Pong {
	
	/**
	 * initially positive.
	 */
	private int _number = 1;
	public int getNumber() {
		return _number;
	}
	public void setNumber(int number) {
		_number = number;
	}
	
}
