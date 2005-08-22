package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class AppContainer implements IAppContainer {

	// TODO: pending use of dependency injection.
	
	public final static IAppContainer INSTANCE = new AppContainer();
	private final Class[] _NO_ARGS_TYPES = new Class[]{};
	private final Object[] _NO_ARGS = new Object[]{};
	
	private IClock _clock = new PcClock();
	
	private AppContainer() {
	}

	public <V> V createTransient(Class<V> javaClass) {
		try {
			Constructor<V> c = javaClass.getDeclaredConstructor(_NO_ARGS_TYPES);
			return c.newInstance(_NO_ARGS);
		} catch (SecurityException ex) {
			throw new RuntimeException("All pojos must have a public no-arg constructor", ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("All pojos must have a public no-arg constructor", ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Failed to invoke no-arg constructor", ex);
		} catch (InstantiationException ex) {
			throw new RuntimeException("Failed to invoke no-arg constructor", ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException("Failed to invoke no-arg constructor", ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException("Failed to invoke no-arg constructor", ex);
		}
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IAppContainer#isPersistent(java.lang.Object)
	 */
	public boolean isPersistent(Object pojo) {
		throw new RuntimeException("Not yet implemented");
	}

	public void delete(Object pojo) {
		throw new RuntimeException("Not yet implemented");
	}

	public Date now() {
		return _clock.now();
	}
	
	
	// TODO: dependency injection
	public IClock getClock() {
		return _clock;
	}
	// TODO: dependency injection
	public void setClock(IClock clock) {
		if (clock == null) {
			throw new IllegalArgumentException("Cannot be null");
		}
		_clock = clock;
	}
	

}
