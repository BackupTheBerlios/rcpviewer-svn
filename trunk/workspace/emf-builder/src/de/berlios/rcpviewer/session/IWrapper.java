package de.berlios.rcpviewer.session;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

/**
 * Provides services to obtain {@link IDomainObject} from a pojo.
 * 
 * <p>
 * Used predominantly by {@link ISession}, but factored out of ISession because 
 * it is not part of session's core responsibility (to whit: maintaining a 
 * footprint of instantiated and attached pojos).
 * 
 * @author Dan Haywood
 *
 */
public interface IWrapper {

	/**
	 * Returns the IDomainObject associated with the pojo.
	 * 
	 * <p>
	 * 
	 * @param pojo
	 * @return
	 */
	IDomainObject getDomainObjectFor(Object pojo);


}
