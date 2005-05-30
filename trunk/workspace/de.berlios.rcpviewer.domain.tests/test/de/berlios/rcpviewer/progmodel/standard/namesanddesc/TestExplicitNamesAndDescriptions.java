package de.berlios.rcpviewer.progmodel.standard.namesanddesc;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.namesanddesc.Appointment;
import de.berlios.rcpviewer.progmodel.standard.namesanddesc.CustomerWithNoExplicitName;
import de.berlios.rcpviewer.progmodel.standard.namesanddesc.ProspectiveSale;

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

	public TestExplicitNamesAndDescriptions(IDeploymentSpecifics domainSpecifics, IDomainAnalyzer domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	public void testDomainClassThatIsExplicitlyNamed() {
		domainClass = lookupAny(ProspectiveSale.class);
		
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
