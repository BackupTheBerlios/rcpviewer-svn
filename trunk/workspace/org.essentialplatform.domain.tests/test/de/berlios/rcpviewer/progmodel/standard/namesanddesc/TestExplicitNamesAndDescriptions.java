package de.berlios.rcpviewer.progmodel.standard.namesanddesc;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

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
public abstract class TestExplicitNamesAndDescriptions extends AbstractTestCase {

	public TestExplicitNamesAndDescriptions(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	public void testDomainClassThatIsExplicitlyNamed() {
		domainClass = lookupAny(ProspectiveSale.class);
		
		assertEquals("Customer", domainClass.getName());
		assertEquals("ProspectiveSale", domainClass.getEClass().getName());
		assertEquals("ProspectiveSale", domainClass.getEClassName());
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
		domainClass = lookupAny(CustomerWithNoExplicitName.class);
		
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
		domainClass = lookupAny(Appointment.class);

		EOperation eOperation = domainClass.getEOperationNamed("moveTo");
		IDomainClass.IOperation operation = domainClass.getOperation(eOperation);
		assertEquals("moveTo", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertEquals("newPeriod", operation.getNameFor(0));
		assertEquals("The time when the appointment should now be scheduled", operation.getDescriptionFor(0));
		assertEquals("rationale", operation.getNameFor(1));
		assertEquals("The reasoning for moving the appointment", operation.getDescriptionFor(1));
		
		eOperation = domainClass.getEOperationNamed("createAt");
		operation = domainClass.getOperation(eOperation);
		assertEquals("createAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertEquals("timePeriod", operation.getNameFor(0));
		assertEquals("When the appointment is to run to and from", operation.getDescriptionFor(0));
		assertEquals("agenda", operation.getNameFor(1));
		assertEquals("The agenda for this appointment", operation.getDescriptionFor(1));
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
