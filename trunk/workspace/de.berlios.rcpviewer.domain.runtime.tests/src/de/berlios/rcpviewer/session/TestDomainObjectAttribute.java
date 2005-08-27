package de.berlios.rcpviewer.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

public class TestDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestDomainObjectAttribute() {
		super(new ExtendedProgModelDomainBuilder());
	}

	public void testCanGetAttribute() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.getPojo().setName("HR");
		IDomainObject.IAttribute nameAttribute = domainObject.getAttribute(domainObject.getEAttributeNamed("name"));
		String value = (String)nameAttribute.get();
		assertEquals("HR", value);
	}

	public void testCanSetAttribute() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		IDomainObject.IAttribute nameAttribute = domainObject.getAttribute(domainObject.getEAttributeNamed("name"));
		nameAttribute.set("HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		IDomainObject.IAttribute nameAttribute = domainObject.getAttribute(domainObject.getEAttributeNamed("name"));
		MyDomainObjectAttributeListener l = 
			nameAttribute.addListener(new MyDomainObjectAttributeListener());
		nameAttribute.set("HR");
		assertTrue(l.attributeChangedCallbackCalled);
	}

	public void testCannotSetAttributeToObjectOfWrongType() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		IDomainObject.IAttribute nameAttribute = domainObject.getAttribute(domainObject.getEAttributeNamed("name"));
		try {
			nameAttribute.set(new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}


}
