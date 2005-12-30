package demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Transient;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.session.IClientSession;
import org.essentialplatform.runtime.shared.session.ClientSessionManager;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

@Lifecycle(instantiable=true)
@InDomain
@javax.persistence.Entity(access=AccessType.PROPERTY)
public class Customer {

	
	private Integer id;
	@RelativeOrder(1)
	@Id(assignedBy=AssignmentType.OBJECT_STORE)
	@javax.persistence.Id(generate=GeneratorType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	private String firstName;
	@RelativeOrder(2)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private String middleInitial;
	@RelativeOrder(3)
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	
	
	private String lastName;
	@RelativeOrder(4)
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

	
	private int age;
	@RelativeOrder(5)
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	private List<Order> orders = new ArrayList<Order>();
	@TypeOf(Order.class)
	@RelativeOrder(6)
	@javax.persistence.OneToMany(mappedBy="customer")
	@javax.persistence.OrderBy("date")
	public List<Order> getOrders() {
		return orders;
	}
	/**
	 * Immutable.
	 * 
	 * <p>
	 * Setter is required for object store (<tt>private</tt> visibility is 
	 * sufficient).
	 * 
	 * @param customer
	 */
	private void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	private void addToOrders(final Order order) {
		orders.add(order);
	}
	
	
	public Order placeOrder(String description) {
		Order order = Order.create(this, description);
		addToOrders(order);
		return order;
	}

	/**
	 * Factory method.
	 * 
	 * @param customer
	 * @return
	 */
	public static Customer create() {
		// following instantiates an Customer
		// yes, it's horrible, but will be replaced by a simple pointcut on {@InDomain).new
		IClientSession session = ClientSessionManager.instance().getCurrentSession(Domain.domainFor(Customer.class));
		IDomainObject<Customer> customerDo = session.create(Domain.lookupAny(Customer.class));
		Customer customer = customerDo.getPojo();
		return customer;
	}
	

}
