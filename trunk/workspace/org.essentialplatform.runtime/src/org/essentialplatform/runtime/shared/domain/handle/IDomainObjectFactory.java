package org.essentialplatform.runtime.shared.domain.handle;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * For {@link org.essentialplatform.runtime.shared.domain.handle.AutoAddingHandleMap} to be able
 * to create objects as required.
 * 
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectFactory {


	/**
	 * Create a domain object for the supplied handle.
	 * 
	 * @param domainClass
	 * @return
	 */
	<T> IDomainObject<T> createDomainObject(Handle handle);

	/**
	 * Create a domain object of the supplied class.
	 * 
	 * <p>
	 * The handle should be assigned using the {@link #getHandleAssigner()}.
	 * 
	 * @param domainClass
	 * @return
	 */
	<T> IDomainObject<T> createDomainObject(IDomainClass domainClass);
	
	/**
	 * The {@link SessionBinding} of any objects created.
	 * 
	 * @return
	 */
	SessionBinding getSessionBinding();

	/**
	 * The {@link IHandleAssigner} to use for assigning handles to the newly
	 * created objects.
	 * 
	 * @return
	 */
	public IHandleAssigner getHandleAssigner();
	
	/**
	 * The initial {@link PersistState} of any objects created.
	 *  
	 * @return
	 */
	PersistState getInitialPersistState();

	/**
	 * The initial {@link ResolveState} of any objects created.
	 *  
	 * @return
	 */
	ResolveState getInitialResolveState();

}
