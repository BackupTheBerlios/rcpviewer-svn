package org.essentialplatform.runtime.fixture.session;

import static org.essentialplatform.progmodel.essential.app.IPrerequisites.Constraint.INVISIBLE;
import static org.essentialplatform.progmodel.essential.app.Prerequisites.require;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;

/**
 * This class is not observed, but has state that affects the prerequisites of
 * the {@link org.essentialplatform.runtime.fixture.session.Ping} that references it.
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
