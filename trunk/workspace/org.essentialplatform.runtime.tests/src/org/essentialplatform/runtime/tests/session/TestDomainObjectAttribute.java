package org.essentialplatform.runtime.tests.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.runtime.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.session.Department;

public class TestDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestDomainObjectAttribute() {
		super(null);
	}

	public void testCanGetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		Department pojo = dobj.getPojo();
		pojo.setName("HR");  // set directly rather than using attrib.set()
		String value = (String)attrib.get();
		assertEquals("HR", value);
	}

	public void testCanSetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		Department pojo = dobj.getPojo();
		attrib.set("HR");
		assertEquals("HR", pojo.getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IDomainClass dc =  lookupAny(Department.class);
		IDomainObject<Department> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		MyDomainObjectAttributeListener l = 
			attrib.addListener(new MyDomainObjectAttributeListener());
		attrib.set("HR");
		assertTrue(l.attributeChangedCallbackCalled);
	}

	public void testCannotSetAttributeToObjectOfWrongType() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		try {
			attrib.set(new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}


}
