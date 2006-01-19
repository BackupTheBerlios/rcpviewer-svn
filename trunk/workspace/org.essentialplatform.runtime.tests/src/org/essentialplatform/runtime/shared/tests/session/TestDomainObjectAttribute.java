package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.domain.bindings.IObjectAttributeClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;

public class TestDomainObjectAttribute extends AbstractRuntimeClientTestCase  {

	public TestDomainObjectAttribute() {
		super(null);
	}

	public void testCanGetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = clientSession.create(dc);
		IDomainClass.IAttribute iAttrib = dobj.getIAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(iAttrib);
		
		Department pojo = dobj.getPojo();
		pojo.setName("HR");  // set directly rather than using attrib.set()
		String value = (String)attrib.get();
		assertEquals("HR", value);
	}

	public void testCanSetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = clientSession.create(dc);
		IDomainClass.IAttribute iAttrib = dobj.getIAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(iAttrib);
		
		Department pojo = dobj.getPojo();
		attrib.set("HR");
		assertEquals("HR", pojo.getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IDomainClass dc =  lookupAny(Department.class);
		IDomainObject<Department> dobj = clientSession.create(dc);
		IDomainClass.IAttribute iAttrib = dobj.getIAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(iAttrib);
		
		IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding(); 
		MyDomainObjectAttributeListener l = 
			atBinding.addListener(new MyDomainObjectAttributeListener());
		attrib.set("HR");
		assertTrue(l.attributeChangedCallbackCalled);
	}

	public void testCannotSetAttributeToObjectOfWrongType() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<Department> dobj = clientSession.create(dc);
		IDomainClass.IAttribute eAttrib = dobj.getIAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		try {
			attrib.set(new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}


}
