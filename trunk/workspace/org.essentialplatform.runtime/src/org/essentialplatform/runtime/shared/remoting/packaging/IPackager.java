package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Package and unpackage pojos so that they can be marshalled.
 * 
 * <p>
 * The process of packaging is 1-method-call affair: invoke {@link #pack(IPojo)}.
 * Unpacking however consists of 3 separate steps:
 * <ol>
 * <li> call {@link #unpackSessionBinding(Object)} to locate the session into
 *      which to unpack the domain object.
 * <li> call {@link #unpackHandle(Object)} to obtain a handle to the actual
 *      domain object, if any, in the session already identified
 * <li> call {@link #merge(IDomainObject, Object)} to merge the unpacked pojo
 *      over the IDomainObject that will represent it within the session.
 * </ol>
 * <p>
 * The caller may need to actually create the IDomainObject/pojo if after 
 * step (2) it is unable to locate the IDomainObject.  For example, on the
 * client this might be when resolving a new object in the object graph; on the
 * server this would be when an object has been newly created on the client and
 * requires persisting. 
 * 
 * @author Dan Haywood
 */
public interface IPackager {

	/**
	 * Package the pojo into an implementation-specific (probably serializable)
	 * representation.
	 * 
	 * @param pojo
	 * @return
	 */
	Object pack(IPojo pojo);

	/**
	 * Obtain the session binding of a packaged-up pojo (such that the session
	 * that manages it can be located).
	 * 
	 * @param packagedPojo
	 * @return
	 */
	SessionBinding unpackSessionBinding(Object packagedPojo);

	/**
	 * Obtain the handle of a packaged-up pojo (such that it can be located in
	 * the session obtained after previously call of {@link #unpackSessionBinding(Object)}.
	 * 
	 * @param packagedPojo
	 * @return
	 */
	Handle unpackHandle(Object packagedPojo);

	/**
	 * Apply (merge) the state of the packaged-up pojo into the supplied
	 * {@link IDomainObject} that represents it within the session (that is,
	 * as located using the Handle returned by {@link #unpackHandle(Object)}, 
	 * or newly created if no such IDomainObject was known to the unpackaging
	 * runtime).
	 *  
	 * @param domainObject - represents the pojo in the client
	 * @param packagedPojo - the 
	 */
	void merge(IDomainObject domainObject, IHandleMap handleMap, Object packagedPojo);
}
