package org.essentialplatform.runtime.tests.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.runtime.RuntimeDeployment.RuntimeDomainBinding;
import org.essentialplatform.runtime.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.session.OrderConstrained;

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
		IDomainClass.IAttribute classAttrib = domainClass.getIAttribute(eAttrib);

		// set expectations
		IFeatureId feature = classAttrib.getFeatureId();
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
		IDomainClass.IAttribute classAttrib = domainClass.getIAttribute(eAttrib);

		// set expectations
		IFeatureId featureId = classAttrib.getFeatureId();
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
