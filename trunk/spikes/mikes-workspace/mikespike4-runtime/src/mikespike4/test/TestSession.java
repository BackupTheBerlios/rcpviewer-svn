package mikespike4.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Facilitates GUI testing.
 * <br>Any GUI tests that wish to handle modal events can use this type
 * in conjunction with classes within this package.
 * <br>Such a class will record rather the modal event within the current 
 * TestSession rather than carry it out.  When the session is stopped all the
 * recorded events can then be checked.
 * @author Mike
 *
 */
public class TestSession {
	
	private static TestSession instance = null;
	
	private final List<Object> events;
	
	/* static methods */
	
	/**
	 * Indicates whether there is an ongoing test session.
	 * @return
	 */
	public static final boolean isActive() {
		return instance != null;
	}
	
	/**
	 * Starts a new test session or throws an exception if one is
	 * already started.
	 * @throws IllegalStateException
	 */
	public static void start() throws IllegalStateException {
		if ( isActive() ) throw new IllegalStateException();
		instance = new TestSession();
	}
	
	/**
	 * Records an event on the current test session or throws an exception
	 * is one has not been started.
	 * @param event
	 */
	public static void recordEvent( Object event ) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( !isActive() ) throw new IllegalStateException();
		instance.events.add( event );
	}
	
	/**
	 * Stops the existing test session returning it for inspection or 
	 * throws an exception if one has not been started.
	 * @throws IllegalStateException
	 */
	public static TestSession stop() throws IllegalStateException {
		if ( !isActive() ) throw new IllegalStateException();
		TestSession finished = instance;
		instance = null;
		return finished;
	}
	
	
	
	/* instance methods */
	
	/**
	 * Returns all events recorded during this session.
	 * @return
	 */
	public List<Object> getEvents() {
		return events;
	}
	
	
	/* private methods */
	
	// singleton constructor
	private TestSession() {
		 events = new ArrayList<Object>();
	}
	
}
