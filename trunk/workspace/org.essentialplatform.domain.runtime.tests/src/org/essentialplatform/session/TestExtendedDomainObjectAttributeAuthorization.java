package org.essentialplatform.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.authorization.IAuthorizationManager;
import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.runtime.RuntimeDeployment.RuntimeDomainBinding;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.IFeatureId;

public class TestExtendedDomainObjectAttributeAuthorization extends AbstractRuntimeTestCase  {

	public void testCanSetAttributeIfDefaultAuthorizationManagerConfigured() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);
		
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
		
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		IDomain domain = getDomainInstance();
		RuntimeDomainBinding binding = (RuntimeDomainBinding)domain.getBinding();
		binding.setAuthorizationManager(authorizationManager);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);
		
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
		
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		IDomain domain = getDomainInstance(); 
		RuntimeDomainBinding binding = (RuntimeDomainBinding)domain.getBinding();
		binding.setAuthorizationManager(authorizationManager);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);

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
