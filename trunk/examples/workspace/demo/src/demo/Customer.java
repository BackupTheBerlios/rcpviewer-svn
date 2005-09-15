package demo;

import java.util.ArrayList;
import java.util.List;

import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.Programmatic;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.session.local.SessionManager;

@Lifecycle(instantiable=true)
@InDomain
public class Customer {

	
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public void changeName(
			@Named("Last name")
			final String lastName) {
		this.lastName = lastName;
	}


	
	private List<Order> orders = new ArrayList<Order>();
	@TypeOf(Order.class)
	public List<Order> getOrders() {
		return orders;
	}
	private void addToOrders(final Order order) {
		orders.add(order);
	}
	
	
	public Order placeOrder() {
		// following instantiates an Order
		// yes, it's horrible, but will be replaced by a simple pointcut on {@InDomain).new
		ISession session = SessionManager.instance().get(SessionManager.instance().getCurrentSessionId());
		IDomainObject<Order> orderDo = session.create(RuntimeDomain.lookupAny(Order.class));
		Order order = orderDo.getPojo();
		addToOrders(order);
		return order;
	}
	
}
