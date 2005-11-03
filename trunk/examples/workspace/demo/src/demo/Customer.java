package demo;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Lifecycle;
import org.essentialplatform.progmodel.extended.Named;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.extended.RelativeOrder;
import org.essentialplatform.progmodel.standard.InDomain;
import org.essentialplatform.progmodel.standard.Programmatic;
import org.essentialplatform.progmodel.standard.TypeOf;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.session.local.Session;
import org.essentialplatform.runtime.session.local.SessionManager;

@Lifecycle(instantiable=true)
@InDomain
public class Customer {

	
	private String firstName;
	@RelativeOrder(1)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private String middleInitial;
	@RelativeOrder(2)
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	
	private int age;
	@RelativeOrder(4)
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	private String lastName;
	@RelativeOrder(3)
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
		IDomainObject<Order> orderDo = session.create(Domain.lookupAny(Order.class));
		Order order = orderDo.getPojo();
		addToOrders(order);
		return order;
	}
//	public Order placeOrder(Product p) {
//		// following instantiates an Order
//		// yes, it's horrible, but will be replaced by a simple pointcut on {@InDomain).new
//		ISession session = SessionManager.instance().get(SessionManager.instance().getCurrentSessionId());
//		IDomainObject<Order> orderDo = session.create(RuntimeDomain.lookupAny(Order.class));
//		Order order = orderDo.getPojo();
//		order.setProduct(p);
//		addToOrders(order);
//		return order;
//	}
	
	
	
	
	
}
