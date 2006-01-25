package org.essentialplatform.runtime.shared.remoting.packaging;

import java.util.List;

import org.essentialplatform.runtime.shared.transaction.changes.IChange;


/**
 * Represents an {@link ITransaction} that has been packaged up.
 * 
 * <p>
 * Unlike some of the other interfaces, doesn't unpackage as an ITransaction.
 * That's because ITransactions are only used client-side.  Rather, it
 * returns the changes that make up the transaction.  Each such 
 * {@link IChange} will be able to furnish (as {@link IPojoPackage}s) the 
 * pojos that it effects.
 * 
 * @author Dan Haywood
 */
public interface ITransactionPackage {

	/**
	 * The pojos enlisted into (modified by) the ITransaction that this package
	 * represents.
	 * 
	 * <p>
	 * The pojos are themselves returned as {@link IPojoPackage}s.  The expected
	 * usage is to call {@link IPackager#merge(IDomainObject, IPojoPackage, IHandleMap)}
	 * for each such pojo package.
	 * 
	 * @return
	 */
	List<IPojoPackage> enlistedPojos();
	
}
