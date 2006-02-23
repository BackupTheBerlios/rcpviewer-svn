package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;

/**
 * Represents a pojo that has been packaged up.
 * 
 * <p>
 * Doesn't expose the structural features of the pojo being packaged - it is
 * the responsibility of the {@link IPackager} to assemble the pojo.  The
 * methods provided her represent meta-data such that the user of the package
 * (client- or server-side) knows what to <i>do</i> with the package in the
 * first place.
 * 
 * @author Dan Haywood
 */
public interface IPojoPackage extends ISessionBindingPackage, IHandlePackage {
	
	/**
	 * The {@link PersistState} of the pojo packaged within.
	 */
	PersistState unpackPersistState();

	/**
	 * The {@link ResolveState} of the pojo packaged within.
	 * 
	 * <p>
	 * The resolve state is only relevant client-side, not server-side.  On
	 * the server it will always be unpacked as {@link ResolveState#UPDATING}.
	 */
	ResolveState unpackResolveState();
}
