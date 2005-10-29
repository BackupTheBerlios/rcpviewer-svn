package de.berlios.rcpviewer.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

public class TestDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestDomainObjectAttribute() {
		super(null);
	}

	public void testCanGetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<?> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		Department pojo = (Department)dobj.getPojo();
		pojo.setName("HR");  // set directly rather than using attrib.set()
		String value = (String)attrib.get();
		assertEquals("HR", value);
	}

	public void testCanSetAttribute() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<?> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		Department pojo = (Department)dobj.getPojo();
		attrib.set("HR");
		assertEquals("HR", pojo.getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IDomainClass dc =  lookupAny(Department.class);
		IDomainObject<?> dobj = session.create(dc);
		EAttribute eAttrib = dobj.getEAttributeNamed("name");
		IDomainObject.IObjectAttribute attrib = dobj.getAttribute(eAttrib);
		
		MyDomainObjectAttributeListener l = 
			attrib.addListener(new MyDomainObjectAttributeListener());
		attrib.set("HR");
		assertTrue(l.attributeChangedCallbackCalled);
	}

	public void testCannotSetAttributeToObjectOfWrongType() {
		IDomainClass dc = lookupAny(Department.class);
		IDomainObject<?> dobj = session.create(dc);
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
