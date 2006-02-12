package demo;

import java.util.Date;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.GeneratorType;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Invisible;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

@Lifecycle(instantiable=true)
@InDomain
@javax.persistence.Entity(access=AccessType.PROPERTY)
@javax.persistence.Table(name="MyOrder")
public class Order {

	
	
	private Integer id;
	@Invisible
	@Id(assignedBy=AssignmentType.OBJECT_STORE)
	@javax.persistence.Id(generate=GeneratorType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
	private Customer customer;
	@RelativeOrder(1)
	@javax.persistence.ManyToOne(cascade={CascadeType.ALL})
	public Customer getCustomer() {
		return customer;
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
	private void setCustomer(Customer customer) {
		this.customer = customer;
	}
	/**
	 * Currently needed for Essential programming model.
	 * 
	 * @param customer
	 */
	public void associateCustomer(Customer customer) {
		setCustomer(customer);
	}
	/**
	 * Currently needed for Essential programming model.
	 * 
	 * @param customer
	 */
	public void dissociateCustomer(Customer customer) {
		setCustomer(null);
	}
	
	
	private Date date;
	@RelativeOrder(2)
	public Date getDate() {
		return date;
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
	private void setDate(Date date) {
		this.date = date;
	}
	

	private int quantity;
	@RelativeOrder(3)
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	private String description;
	@RelativeOrder(4)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	/**
	 * Factory method.
	 * 
	 * @param customer
	 * @return
	 */
	static Order create(final Customer customer, final String description) {
		// following instantiates an Order
		// yes, it's horrible, but will be replaced by a simple pointcut on {@InDomain).new
		IClientSession session = ClientSessionManager.instance().getCurrentSession(Domain.domainFor(Order.class));
		IDomainObject<Order> orderDo = session.create(Domain.lookupAny(Order.class));
		Order order = orderDo.getPojo();
		order.customer = customer;
		order.date = new Date();
		order.setDescription(description);
		return order;
	}
	
	// static Order create(final Customer customer, Product p, final String description) {


}
