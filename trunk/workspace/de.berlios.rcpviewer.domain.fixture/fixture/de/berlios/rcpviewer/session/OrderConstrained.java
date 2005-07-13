package de.berlios.rcpviewer.session;

import static de.berlios.rcpviewer.progmodel.extended.IPrerequisites.Constraint.INVISIBLE;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.require;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class OrderConstrained {
	
	private int quantity;
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * Cannot change once shipped.
	 *  
	 * @return
	 */
	public IPrerequisites getQuantityPre() {
		return 
			require(!isShipped(), "Cannot change quantity once shipped")
			.andRequire(!isRestricted(), INVISIBLE);
	}
	
	private boolean shipped;
	public boolean isShipped() {
		return shipped;
	}
	private void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	private boolean restricted;
	public boolean isRestricted() {
		return restricted;
	}
	private void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
	
	public void ship() {
		setShipped(true);
	}
	public IPrerequisites shipPre() {
		return 
			require(!isShipped(), "Cannot change quantity once shipped")
			.andRequire(!isRestricted(), INVISIBLE);
	}
	
	public void shipAndRestrict() {
		ship();
		setRestricted(true);
	}
	

}
