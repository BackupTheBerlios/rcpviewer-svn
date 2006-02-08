package org.essentialplatform.runtime.shared;

import org.apache.log4j.Logger;


/**
 * Skeleton implementation of a {@link IServer}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractSimpleLifecycle {

	private boolean _started;

	protected abstract Logger getLogger();
	
	protected AbstractSimpleLifecycle(String description) {
		_description = description;
	}

	private String _description;

	public final void start() {
		assertNotStarted();
		getLogger().debug(String.format(">>>>>>>>>> STARTING %s '%s' >>>>>>>>>>>>>>", _description, this.getClass().getSimpleName()));
		if (doStart()) {
			_started = true;
			getLogger().debug(String.format(">>>>>>>>>> STARTING %s '%s' >> OK >>>>>>>>", _description, this.getClass().getSimpleName()));
		} else {
			getLogger().error(String.format(">>>>>>>>>> STARTING %s '%s' >> FAILED >>>>", _description, this.getClass().getSimpleName()));
		}
	}

	/**
	 * Hook method; will only be called if the service is known to be
	 * not started (as per {@link #isStarted()}).
	 * 
	 * @return <tt>true</tt> if successfully started.
	 */
	protected boolean doStart() {
		return true;
	}

	
	public final void shutdown() {
		assertStarted();
		getLogger().debug(String.format("<<<<<<<<<< SHUTTING DOWN %s '%s' <<<<<<<<<<<<<<", _description, this.getClass().getSimpleName()));
		if (doShutdown()) {
			_started = false;
			getLogger().debug(String.format("<<<<<<<<<< SHUTTING DOWN %s '%s' << OK <<<<<<<<", _description, this.getClass().getSimpleName()));
		} else {
			getLogger().error(String.format("<<<<<<<<<< SHUTTING DOWN %s '%s' << FAILED <<<<", _description, this.getClass().getSimpleName()));
		}
	}


	/**
	 * Hook method; will only be called if the service is known to be
	 * started (as per {@link #isStarted()}).
	 * 
	 * @return <tt>true</tt> if successfully shutdown.
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


