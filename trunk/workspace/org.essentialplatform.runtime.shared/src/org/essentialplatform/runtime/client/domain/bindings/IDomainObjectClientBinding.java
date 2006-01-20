package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectListener;
import org.essentialplatform.runtime.client.session.ClientSession;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;


/**
 * Represents client-specific functionality for an <i>instance of</i> a 
 * {@link IDomainClass} (that is, an {@link IDomainObject}).
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectClientBinding<T> extends IDomainObjectRuntimeBinding<T> {

	///////////////////////////////////////////////////////////
	// title()
	///////////////////////////////////////////////////////////
	
	/**
	 * Distinguishable representation of the domain object in the UI.
	 * 
	 * <p>
	 * TODO: should this be required to be unique.  If not, how
	 * 
	 * @return
	 */
	public String title();


	///////////////////////////////////////////////////////////
	// ClientSession, isAttached, attach, detach
	///////////////////////////////////////////////////////////
	
	/**
	 * The {@link IClientSession} to which this domain object is currently attached.
	 * 
	 * <p>
	 * The implementation is not required to serialize this information.
	 * 
	 * @return
	 */
	public IClientSession getSession();
	
	
	/**
	 * Whether this domain object is currently attached to a {@link IClientSession}.
	 * 
	 * <p>
	 * If so, then {#getSession()} will return a non-null result.
	 *  
	 * @return
	 */
	public boolean isAttached();




	///////////////////////////////////////////////////////////
	// Listeners
	///////////////////////////////////////////////////////////
	
	/**
	 * Adds domain object listener.
	 * 
	 * <p>
	 * If the listener is already known, does nothing.
	 * 
	 * <p>
	 * Note: we return the listener only because it slightly simplfies the
	 * implementation of tests.
	 * 
	 * @param listener
	 */
	public <V extends IDomainObjectListener> V addListener(V listener);

	
	/**
	 * Removes domain object listener.
	 * 
	 * <p>
	 * If the listener is unknown, does nothing.
	 * 
	 * @param listener
	 */
	public void removeListener(IDomainObjectListener listener);


	/**
	 * Ensures that the session binding is compatible.
	 */
	public void attached(IClientSession session);

	public void detached();


}