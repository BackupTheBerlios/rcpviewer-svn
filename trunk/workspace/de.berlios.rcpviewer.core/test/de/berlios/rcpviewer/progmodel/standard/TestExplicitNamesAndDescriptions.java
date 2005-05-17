package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.Domain;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

/**
 * Tests for the use of the <tt>Named</tt> and <tt>DescribedAs</tt>.
 * 
 * <p>
 * There is some repetition with tests elsewhere in terms of tests relating to 
 * features with no explicit name - this test case tests edge conditions more
 * exhaustively.
 * 
 * @author Dan Haywood
 */
public class TestExplicitNamesAndDescriptions extends AbstractTestCase {

	@InDomain
	public static class CustomerWithNoExplicitName {
	}
	@Named("Customer")
	@DescribedAs("A Customer who may have originally become known to us via " +
			     "the marketing system or who may have contacted us directly.")
	@InDomain
	public static class ProspectiveSale {
	}
	public static class TimePeriod implements ValueMarker {
		private java.util.Date from;
		public java.util.Date getFrom() {
			return from;
		}
		public void setFrom(java.util.Date from) {
			this.from = from;
		}
		private java.util.Date to;
		public java.util.Date getTo() {
			return to;
		}
		public void setTo(java.util.Date to) {
			this.to = to;
		}
	}
	@InDomain
	public static class Appointment {
		public void moveTo(
				@Named("newPeriod")
				@DescribedAs("The time when the appointment should now be scheduled")
				TimePeriod newPeriod, 
				@Named("rationale")
				@DescribedAs("The reasoning for moving the appointment")
				String rationale) {
		}
		public static void createAt(
				@Named("timePeriod")
				@DescribedAs("When the appointment is to run to and from")
				TimePeriod timePeriod,
				@Named("agenda")
				@DescribedAs("The agenda for this appointment")
				String agenda) {
		}
	}

	private Domain domain;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	

	public void testDomainClassThatIsExplicitlyNamed() {
		domainClass = new DomainClass<ProspectiveSale>(ProspectiveSale.class);
		assertEquals("Customer", domainClass.getName());
		assertEquals("Customer", domainClass.getEClass().getName());
		assertEquals(
				"A Customer who may have originally become known to us via " +
			    "the marketing system or who may have contacted us directly.", 
			    domainClass.getDescription());
		assertEquals(
				"A Customer who may have originally become known to us via " +
			    "the marketing system or who may have contacted us directly.", 
			    domainClass.getDescription());
	}

	public void testDomainClassThatIsNotExplicitlyNamed() {
		domainClass = new DomainClass<CustomerWithNoExplicitName>(CustomerWithNoExplicitName.class);
		assertEquals("CustomerWithNoExplicitName", domainClass.getName());
		assertEquals("CustomerWithNoExplicitName", domainClass.getEClass().getName());
		assertNull(domainClass.getDescription());
	}

	public void incompletetestAttributeThatIsExplicitlyNamed() {
		// TODO
	}

	public void incompletetestAttributeThatIsNotExplicitlyNamed() {
		// TODO
	}
	
	public void incompletetestOperationThatIsExplicitlyNamed() {
		// TODO
	}

	public void incompletetestOperationThatIsNotExplicitlyNamed() {
		// TODO
	}

	
	public void testOperationParameterThatIsExplicitlyNamed() {
		// 2 arg
		domainClass = domain.lookup(Appointment.class);
		domain.done();

		EOperation eOperation = domainClass.getEOperationNamed("moveTo");
		assertEquals("moveTo", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertEquals("newPeriod", domainClass.getNameFor(eOperation, 0));
		assertEquals("The time when the appointment should now be scheduled", domainClass.getDescriptionFor(eOperation, 0));
		assertEquals("rationale", domainClass.getNameFor(eOperation, 1));
		assertEquals("The reasoning for moving the appointment", domainClass.getDescriptionFor(eOperation, 1));
		
		eOperation = domainClass.getEOperationNamed("createAt");
		assertEquals("createAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertEquals("timePeriod", domainClass.getNameFor(eOperation, 0));
		assertEquals("When the appointment is to run to and from", domainClass.getDescriptionFor(eOperation, 0));
		assertEquals("agenda", domainClass.getNameFor(eOperation, 1));
		assertEquals("The agenda for this appointment", domainClass.getDescriptionFor(eOperation, 1));
	}


	
	public void incompletetestOperationParameterThatIsNotExplicitlyNamed() {
		// TODO
	}

	
	public void incompletetestLinkThatIsExplicitlyNamed() {
		// TODO
	}

	public void incompletetestLinkThatIsNotExplicitlyNamed() {
		// TODO
	}

	public void incompletetestSomeTestsToDoWithCanonicalizingExplicitNames() {
		// TODO
		
	}
	
	public void incompletetestSomeTestsToDoWithErrorHandlingOfInvalidNames() {
		// TODO
		
	}
	
}
