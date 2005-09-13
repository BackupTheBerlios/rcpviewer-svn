package de.berlios.rcpviewer.transaction;


import java.util.List;
import java.util.ArrayList;

import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;

@InDomain
public class Customer {

	public Customer() {
		super();
	}

	private EmailAddress _emailAddress;
	public EmailAddress getEmailAddress() {
		return _emailAddress;
	}
	public void setEmailAddress(final EmailAddress emailAddress) {
		_emailAddress = emailAddress;
	}
	
	private final List<Order> _orders = new ArrayList<Order>();
	@TypeOf(Order.class)
	public List<Order>getOrders() { return _orders; }
	public void addToOrders(final Order order) {
		_orders.add(order);
	}
	public void removeFromOrders(final Order order) {
		_orders.remove(order);
	}
	
	/**
	 * This is an operation that modifies this object and the provided
	 * object.  Both should be enrolled into the same transaction.
	 *
	 */
	public void useEmailAddress(EmailAddress emailAddress) {
		setEmailAddress(emailAddress);
		emailAddress.setCustomer(this);
	}
	
}
