package org.essentialplatform.server;

import org.apache.log4j.Logger;


/**
 * Skeleton implementation of a {@link Service}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractService implements IService {

	private boolean _started;

	protected abstract Logger getLogger();


	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#start()
	 */
	public final void start() {
		assertNotStarted();
		if (doStart()) {
			_started = true;
		}
	}

	/**
	 * Hook method.
	 * 
	 * @return
	 */
	protected boolean doStart() {
		return true;
	}

	
	public final void shutdown() {
		assertStarted();
		if (doShutdown()) {
			_started = false;
		}
	}


	/**
	 * Hook method.
	 * 
	 * @return
	 */
	protected boolean doShutdown() {
		return true;
	}


	/*
	 * @see org.essentialplatform.server.IService#isStarted()
	 */
	public final boolean isStarted() {
		return _started;
	}

	protected final void assertStarted() {
		if (!isStarted()) {
			throw new IllegalStateException("Not started");
		}
	}
	protected final void assertNotStarted() {
		if (isStarted()) {
			throw new IllegalStateException("Already started");
		}
	}

}


