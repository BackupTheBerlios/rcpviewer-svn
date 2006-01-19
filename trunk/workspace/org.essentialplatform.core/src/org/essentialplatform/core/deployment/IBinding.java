package org.essentialplatform.core.deployment;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.osgi.framework.Bundle;

public interface IBinding {

	public IDomainBinding bindingFor(IDomain domain);

	// JAVA5_FIXME: return type
	public <V extends IDomainClassBinding> V bind(IDomainClass domainClass, Object classRepresentation);

	public <V extends IAttributeBinding> V bindingFor(IAttribute attribute);

	// JAVA5_FIXME: return type
	public <V extends IOneToOneReferenceBinding> V bindingFor(IOneToOneReference oneToOneReference);

	// JAVA5_FIXME: return type
	public <V extends ICollectionReferenceBinding> V bindingFor(ICollectionReference collectionReference);

	// JAVA5_FIXME: return type
	public <V extends IOperationBinding> V bindingFor(IOperation operation);
	
	public IDomainBuilder getPrimaryBuilder();

	public InDomain getInDomainOf(Object classRepresentation);

	
	/**
	 * Ensure that this class representation is valid for this binding.
	 * 
	 * <p>
	 * The implementation does not need to check for null.
	 * 
	 * @param classRepresentation - will be non-null.
	 */
	public void assertValid(final Object classRepresentation);

	public Bundle getBundle();

}
