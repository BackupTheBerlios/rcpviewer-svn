package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.progmodel.extended.ConstraintSet;
import de.berlios.rcpviewer.progmodel.extended.IConstraintSet;
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
	public IConstraintSet getQuantityPre() {
		return ConstraintSet.create()
			.unusableIf(isShipped(), "Cannot change quantity once shipped")
			.invisibleIf(isHidden());
	}
	
	private boolean shipped;
	public boolean isShipped() {
		return shipped;
	}
	private void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	private boolean hidden;
	public boolean isHidden() {
		return hidden;
	}
	private void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public void ship() {
		setShipped(true);
	}
	public IConstraintSet shipPre() {
		return ConstraintSet.create()
			.unusableIf(isShipped(), "Cannot change quantity once shipped")
			.invisibleIf(isHidden());
	}
	
	public void shipAndHide() {
		ship();
		setHidden(true);
	}
	

}
