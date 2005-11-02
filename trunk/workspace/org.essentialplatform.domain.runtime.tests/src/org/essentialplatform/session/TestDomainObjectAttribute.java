package org.essentialplatform.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.authorization.IAuthorizationManager;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.IFeatureId;

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
