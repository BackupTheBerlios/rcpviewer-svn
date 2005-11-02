package org.essentialplatform.session;

import static org.essentialplatform.progmodel.extended.IPrerequisites.Constraint.INVISIBLE;
import static org.essentialplatform.progmodel.extended.Prerequisites.require;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

/**
 * This class is not observed, but has state that affects the prerequisites of
 * the {@link org.essentialplatform.session.Ping} that references it.
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
