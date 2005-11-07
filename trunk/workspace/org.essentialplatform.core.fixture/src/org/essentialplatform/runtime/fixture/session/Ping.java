package org.essentialplatform.runtime.fixture.session;

import static org.essentialplatform.progmodel.essential.app.IPrerequisites.Constraint.INVISIBLE;
import static org.essentialplatform.progmodel.essential.app.Prerequisites.require;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.progmodel.essential.app.IPrerequisites.Constraint;

/**
 * This class is observed, and has prerequisites that depend upon another
 * object.
 * 
 * @author Dan Haywood
 *
 */
@InDomain
public class Ping {

	private Pong _pong;
	public Pong getPong() {
		return _pong;
	}
	public void setPong(Pong pong) {
		_pong = pong;
	}
	
	private int _visibleOnlyIfPongPositive;
	public int getVisibleOnlyIfPongPositive() {
		return _visibleOnlyIfPongPositive;
	}
	public IPrerequisites getVisibleOnlyIfPongPositivePre() {
		return Prerequisites.require(getPong().getNumber() > 0, "Pong must be positive", Constraint.INVISIBLE);
	}

	private int _usableOnlyIfPongPositive;
	public int getUsableOnlyIfPongPositive() {
		return _usableOnlyIfPongPositive;
	}
	public IPrerequisites getUsableOnlyIfPongPositivePre() {
		return Prerequisites.require(getPong().getNumber() > 0, "Pong must be positive");
	}

}
