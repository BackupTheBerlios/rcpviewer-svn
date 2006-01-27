package org.essentialplatform.runtime.client.remoting.packaging;

import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.IMarshallingOptimizer;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Packages transactions.
 * 
 * @author Dan Haywood
 */
public interface IPackager extends IMarshallingOptimizer {

	/**
	 * Package the pojo into an implementation-specific (probably serializable)
	 * representation.
	 * 
	 * @param pojo
	 * @return the packaged representation.
	 */
	<V extends IPojoPackage> V pack(IPojo pojo);

	/**
	 * Package the transaction into an implementation-specific (probably
	 * serializable) representation.
	 * 
	 * @param transaction
	 * @return the packaged representation.
	 */
	<V extends ITransactionPackage> V pack(ITransaction transaction);


}
