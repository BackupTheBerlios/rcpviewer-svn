package org.essentialplatform.progmodel.essential.core.tests;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithCompositeId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIntegerId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIntegerIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleLongId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleLongIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveByteId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveByteIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveIntId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveIntIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveLongId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveLongIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveShortId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveShortIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleShortId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleShortIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.AssignmentType;

/**
 * Tests for the use of the <tt>@Id</tt> annotation.
 * 
 * @author Dan Haywood
 */
public abstract class TestId extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testWhenNoIdentifiers() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		
		List<EAttribute> idAttributes = domainClass.idEAttributes();
		assertEquals(0, idAttributes.size());
	}

	
	public void testRelativeOrderingOfSimpleIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleId.class);
		
		List<EAttribute> idAttributes = domainClass.idEAttributes();
		assertEquals(1, idAttributes.size());
		assertEquals("id", idAttributes.get(0).getName());
	}
	
	public void testRelativeOrderingOfCompositeIdentifier() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		
		List<EAttribute> idAttributes = domainClass.idEAttributes();
		assertEquals(2, idAttributes.size());
		assertEquals("lastName", idAttributes.get(0).getName());
		assertEquals("firstName", idAttributes.get(1).getName());
	}

	public void testIdImplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerId.class);
		
		assertSame(AssignmentType.OBJECT_STORE, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForSimpleNonIntegralIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdImplicitAssignmentTypeForCompositeIdentifier() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}

	//////////
	public void testIdExplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}
	
	public void testIdExplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerIdAssignedByApplication.class);
		
		assertSame(AssignmentType.APPLICATION, domainClass.getIdAssignmentType());
	}

}
