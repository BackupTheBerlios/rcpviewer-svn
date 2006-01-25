package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Unpackages transactions.
 * 
 * <p>
 * Unpacking consists of 3 separate steps:
 * <ol>
 * <li> call {@link #unpackSessionBinding(ISessionBindingPackage)} to locate the session into
 *      which to unpack the domain object.
 * <li> call {@link #unpackHandle(IHandlePackage)} to obtain a handle to the actual
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
public interface IUnpackager extends IMarshallingOptimizer {

	/**
	 * Obtain the session binding of a packaged-up pojo (such that the session
	 * that manages it can be located).
	 * 
	 * @param packagedSessionBinding, or an object that provides a {@link SessionBinding} (such as an {@link IPojoPackage}).
	 * @return the unpackaged {@link SessionBinding}
	 */
	SessionBinding unpackSessionBinding(ISessionBindingPackage packagedSessionBinding);

	/**
	 * Obtain the handle of a packaged-up pojo (such that it can be located in
	 * the session obtained after previously call of {@link #unpackSessionBinding(ISessionBindingPackage)}.
	 * 
	 * @param handlePackage, or a package that provides a handle (such as an {@link IPojoPackage}).
	 * @return the unpackaged {@link Handle}
	 */
	Handle unpackHandle(IHandlePackage handlePackage);

	/**
	 * Apply (merge) the state of the packaged-up pojo into the supplied
	 * {@link IDomainObject} that represents it within the session (that is,
	 * as located using the Handle returned by {@link #unpackHandle(IHandlePackage)}, 
	 * or newly created if no such IDomainObject was known to the unpackaging
	 * runtime).
	 *  
	 * @param domainObject - represents the pojo in the client, to be updated
	 * @param packagedPojo - the values of the pojo state is to be merged in
	 * @param handleMap    - to dereference any handles held in the references and collections of the packaged pojo.
	 */
	void merge(IDomainObject domainObject, IPojoPackage packagedPojo, IHandleMap handleMap);

}
