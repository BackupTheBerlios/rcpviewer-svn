package org.essentialplatform.session;

import static org.essentialplatform.progmodel.extended.IPrerequisites.Constraint.INVISIBLE;
import static org.essentialplatform.progmodel.extended.Prerequisites.require;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

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
	public IPrerequisites setQuantityPre(int quantity) {
		return 
			require(quantity > 0, "Quantity must be positive");
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
