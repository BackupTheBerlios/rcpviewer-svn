package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EOperation;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.namesanddesc.Appointment;
import org.essentialplatform.core.fixture.progmodel.essential.standard.namesanddesc.CustomerWithNoExplicitName;
import org.essentialplatform.core.fixture.progmodel.essential.standard.namesanddesc.ProspectiveSale;
import org.essentialplatform.core.tests.AbstractTestCase;

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

	private IDomainClass domainClass;

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

		IDomainClass.IOperation operation = domainClass.getIOperationNamed("moveTo");
		assertEquals("moveTo", operation.getName());
		assertEquals(2, operation.getEOperation().getEParameters().size());
		assertEquals("newPeriod", operation.getNameFor(0));
		assertEquals("The time when the appointment should now be scheduled", operation.getDescriptionFor(0));
		assertEquals("rationale", operation.getNameFor(1));
		assertEquals("The reasoning for moving the appointment", operation.getDescriptionFor(1));
		
		operation = domainClass.getIOperationNamed("createAt");
		assertEquals("createAt", operation.getEOperation().getName());
		assertEquals(2, operation.getEOperation().getEParameters().size());
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
