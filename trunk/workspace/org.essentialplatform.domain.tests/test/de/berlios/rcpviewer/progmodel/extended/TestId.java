package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>@Id</tt> annotation.
 * 
 * @author Dan Haywood
 */
public abstract class TestId extends AbstractTestCase {

	public TestId(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testWhenNoIdentifiers() {
		domainClass = 
			lookupAny(CustomerWithNoIdentifier.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		List<EAttribute> idAttributes = 
			domainClass.idAttributes();
		assertEquals(0, idAttributes.size());
	}

	
	public void testRelativeOrderingOfSimpleIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		List<EAttribute> idAttributes = domainClass.idAttributes();
		assertEquals(1, idAttributes.size());
		assertEquals("id", idAttributes.get(0).getName());
	}
	
	public void testRelativeOrderingOfCompositeIdentifier() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		List<EAttribute> idAttributes = domainClass.idAttributes();
		assertEquals(2, idAttributes.size());
		assertEquals("lastName", idAttributes.get(0).getName());
		assertEquals("firstName", idAttributes.get(1).getName());
	}

	public void testIdImplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleShortId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleIntegerId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleLongId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveByteId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveShortId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveIntId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveLongId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleBigIntegerId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleNonIntegralIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleStringId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForCompositeIdentifier() {
		domainClass = 
			lookupAny(CustomerWithCompositeId.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}

	//////////
	public void testIdExplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleByteIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleShortIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleIntegerIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleLongIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveByteIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveShortIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveIntIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimplePrimitiveLongIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = 
			lookupAny(CustomerWithSimpleBigIntegerIdAssignedByApplication.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}

}
