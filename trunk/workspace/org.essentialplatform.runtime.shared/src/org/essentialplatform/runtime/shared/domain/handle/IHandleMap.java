package org.essentialplatform.runtime.shared.domain.handle;

import java.util.Iterator;
import java.util.Set;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Double map for converting {@link Handle}s into {@link IDomainObject}s and 
 * back again.
 * 
 * <p>
 * Required by implementations of {@link IPackager} (and would most likely
 * be implemented by client-side session etc.)
 * 
 * 
 * @author Dan Haywood
 */
public interface IHandleMap {
	
	/**
	 * The (domain, objectstore) for which the domain objects in this map
	 * belong.
	 * 
	 * <p>
	 * Implementations are NOT expected to verify the domain.
	 *  
	 * @return
	 */
	SessionBinding getSessionBinding();
	
	/**
	 * Look up pojo from handle.
	 * 
	 * <p>
	 * Implementations (such as session) may need to create or resolve first,
	 * ensuring that the pojo is correctly initialized.
	 * 
	 * <p>
	 * If the {@link IDomainObject} is required, the pojo can be obtained
	 * using {@link IDomainObject#getPojo()}. 
	 * 
	 * <p>
	 * If the domain object cannot be located using the handle, then any
	 * previous value known by the handle (that is as returned by 
	 * {@link Handle#getPrevious()} should be used.  As a side-effect, the
	 * hashes should be transparently updated.  
	 * 
	 * <p>
	 * TODO: suspect that the implementation will need to ensure that the
	 * pojo is connected to the correct session (and so cannot actually be
	 *  
	 * 
	 * @param handle
	 * @return
	 */
	IDomainObject getDomainObject(Handle handle);

	
	/**
	 * Return handle for pojo.
	 * 
	 * <p>
	 * If only the {@link IPojo} is known, the domain object can be obtained
	 * using {@link IPojo#domainObject()}. 
	 * 
	 * @param pojo
	 * @return
	 */
	Handle getHandle(IDomainObject domainObject);


	/**
	 * Adds a pair of tuples (namely, (domainObject, handle) and a reciprocal
	 * (handle, domainObject)) to the map.
	 * 
	 * <p>
	 * It is not necessary to pass in the handle because it is obtainable from 
	 * the {@link IDomainObject} using {@link IDomainObject#getHandle()}.  
	 * 
	 * <p>
	 * If both (domainObject, handle) tuples already exist (and are reciprocals
	 * of each other), then the method does nothing (and returns <tt>false</tt>
	 * to indicate that no domainObject was actually added).  If however the
	 * domainObject is not in the map but its corresponding handle is, or vice
	 * versa that the domainObject is in the map but the handle is not, then
	 * an exception will be thrown.
	 * 
	 * @param domainObject
	 * @return false if the domainObject is already in the maps (with the
	 *         correct handle).
	 */
	boolean add(IDomainObject domainObject) throws IllegalStateException;;
	
	/**
	 * Removes the (domainObject, handle) tuple, and uses the corresponding
	 * handle to remove the reciprocal (handle, domainObject) tuple.  
	 * 
	 * @param domainObject
	 * @return true if an object was located and removed, false otherwise.
	 * @throws IllegalStateException if object could not be located in both hashes.
	 */
	boolean remove(IDomainObject domainObject) throws IllegalStateException;
	
	/**
	 * Removes the (handle, domainObject) tuple, and uses the corresponding
	 * domainObject to remove the reciprocal (domainObject, handle) tuple.  
	 * 
	 * @param domainObject
	 * @return true if an object was located and removed, false otherwise.
	 * @throws IllegalStateException if object could not be located in both hashes.
	 */
	boolean remove(Handle handle) throws IllegalStateException;
	
	/**
	 * Returns an unmodifiable set of the handles to access the domain 
	 * objects within the map.
	 * 
	 * <p>
	 * That is, cannot invoke {@link Iterator#remove()}.
	 *  
	 * @return
	 */
	Set<Handle> handles();

}
