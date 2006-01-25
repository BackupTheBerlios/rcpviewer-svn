package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Represents a packaging that contains a {@link SessionBinding}.
 * 
 * @author Dan Haywood
 */
public interface ISessionBindingPackage {
	
	SessionBinding unpackSessionBinding();

}
