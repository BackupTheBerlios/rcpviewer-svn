package org.essentialplatform.runtime.server;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.AbstractSimpleLifecycle;


/**
 * Skeleton implementation of a {@link IServer}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractServer extends AbstractSimpleLifecycle implements IServer {

	protected AbstractServer() {
		super("SERVICE");
	}
	

}


