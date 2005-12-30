package org.essentialplatform.runtime.shared.tests.session;

import org.easymock.MockControl;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.runtime.client.RuntimeClientBinding.RuntimeClientDomainBinding;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.OrderConstrained;

public class TestExtendedDomainObjectAttributeAuthorization extends AbstractRuntimeTestCase  {

	public void testCanSetAttributeIfDefaultAuthorizationManagerConfigured() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);
		
		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);
		
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
		RuntimeClientDomainBinding binding = (RuntimeClientDomainBinding)domain.getBinding();
		binding.setAuthorizationManager(authorizationManager);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);
		
		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);

		// set expectations
		IFeatureId feature = iAttrib.getFeatureId();
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
		RuntimeClientDomainBinding binding = (RuntimeClientDomainBinding)domain.getBinding();
		binding.setAuthorizationManager(authorizationManager);
		
		IDomainObject<OrderConstrained> domainObject = session.create(domainClass);

		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);

		// set expectations
		IFeatureId featureId = iAttrib.getFeatureId();
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
