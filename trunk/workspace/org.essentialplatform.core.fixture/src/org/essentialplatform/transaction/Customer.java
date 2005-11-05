package org.essentialplatform.transaction;


import java.util.List;
import java.util.ArrayList;

import org.essentialplatform.progmodel.essential.app.IAppContainer;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;

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
	
	private String _firstName;
	public String getFirstName() {
		return _firstName;
	}
	public void setFirstName(final String firstName) {
		_firstName = firstName;
	}
	
}
