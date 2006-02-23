package org.essentialplatform.runtime.server.remoting.xactnprocessor;

import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.client.remoting.packaging.IPackager;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.IUnpackager;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Adapter class.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractTransactionProcessor implements ITransactionProcessor {

	
	public void process(ITransactionPackage transactionPackage) {
		// does nothing
	}


	//////////////////////////////////////////////////////////////////
	// IPackager
	//////////////////////////////////////////////////////////////////
	
	private IUnpackager unpackager;
	public IUnpackager getUnpackager() {
		return unpackager;
	}
	public void setUnpackager(IUnpackager unpackager) {
		this.unpackager = unpackager;
	}


	//////////////////////////////////////////////////////////////////
	// ObjectStoreRouting
	//////////////////////////////////////////////////////////////////
	
	private ObjectStoreRouting _objectStoreRouting = new ObjectStoreRouting();
	public ObjectStoreRouting getObjectStoreRouting() {
		return _objectStoreRouting;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; defaults to a vanilla {@link ObjectStoreRouting}.
	 * 
	 * @param objectStoreRouting
	 */
	public void setObjectStoreRouting(ObjectStoreRouting objectStoreRouting) {
		_objectStoreRouting = objectStoreRouting;
	}
	
}
