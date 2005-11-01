package de.berlios.rcpviewer.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeDomainBinding;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

public class TestExtendedDomainObjectAttributeAuthorization extends AbstractRuntimeTestCase  {

	public TestExtendedDomainObjectAttributeAuthorization() {
		super(new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

	public void testCanSetAttributeIfDefaultAuthorizationManagerConfigured() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		
		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void incompletetestCanSetAttributeIfAuthorizationManagerDoesNotVeto() {
		
	}

	public void testCanSetAttributeIfAuthorizationManagerDoesNotVetoUsability() {
		
		MockControl control = 
			MockControl.createStrictControl(IAuthorizationManager.class);
		IAuthorizationManager authorizationManager = 
			(IAuthorizationManager)control.getMock();
		
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		IDomain domain = getDomainInstance();
		domain.addBuilder(getDomainBuilder());
		((RuntimeDomainBinding)domain.getBinding()).setAuthorizationManager(authorizationManager);
		domain.done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		
		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);

		// set expectations
		IFeatureId feature = classAttrib.attributeIdFor();
		authorizationManager.preconditionsFor(feature);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		control.setReturnValue(Prerequisites.none());
		control.replay();
		
		IPrerequisites prerequisites = attrib.authorizationPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
		
		// verify
		control.verify();
	}
	public void testCannotSetAttributeIfAuthorizationManagerVetoesUsability() {
		MockControl control = 
			MockControl.createStrictControl(IAuthorizationManager.class);
		IAuthorizationManager authorizationManager = 
			(IAuthorizationManager)control.getMock();
		
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		IDomain domain = getDomainInstance(); 
		domain.addBuilder(getDomainBuilder());
		((RuntimeDomainBinding)domain.getBinding()).setAuthorizationManager(authorizationManager);
		domain.done();
		
		IDomainObject<?> domainObject = session.create(domainClass);

		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);

		// set expectations
		IFeatureId featureId = classAttrib.attributeIdFor();
		authorizationManager.preconditionsFor(featureId);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		IPrerequisites returnPrerequisites = Prerequisites.require(false, "Cannot edit quantity"); 
		control.setReturnValue(returnPrerequisites);
		control.replay();
		
		IPrerequisites prerequisites = attrib.authorizationPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		
		// verify
		control.verify();
	}

}
