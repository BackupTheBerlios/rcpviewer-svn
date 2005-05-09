package mikespike2_runtime.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.ListenerList;

/**
 * Anitpattern representing the metamodel.
 * @author Mike
 *
 */
public class God {
	
	/* statics */
	
	private static God instance = null;
	
	// synchronising indicates a lack of belief in there being only one god
	public static final synchronized God getInstance() {
		if ( instance == null ) {
			instance = new God();
		}
		return instance;
	}
	
	
	/* fields */
	
	// want unique list - why not a set? - want a fixed order
	private final List<Class> classes = new ArrayList<Class>();
	
	// a jface class so no really appropriate for a model class!
	private final ListenerList listeners = new ListenerList();
	
	
	/* metamodel contract */
	
	public Class[] getClasses() {
		return (Class[])classes.toArray( new Class[0] );
	}
	
	public boolean addClass( Class clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		if ( classes.contains( clazz ) ) return false;
		if ( classes.add( clazz ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public boolean removeClass( Class clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		if ( !classes.contains( clazz ) ) return false;
		if ( classes.remove( clazz ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public String getName( Class clazz ) {
		if( clazz == null ) throw new IllegalArgumentException();
		if( !classes.contains( clazz) )  throw new IllegalArgumentException();
		return clazz.getSimpleName();
	}
	
	public void reset() {
		classes.clear();
		notifyListeners();
	}
	
	public Object newInstance( Class clazz ) {
		if( clazz == null ) throw new IllegalArgumentException();
		if( !classes.contains( clazz) )  throw new IllegalArgumentException();
		// assume no-arg constructor for now
		try {
			return clazz.newInstance();
		}
		catch ( InstantiationException ie ) {
			assert false : ie.toString();
			return null;
		}
		catch ( IllegalAccessException iae ) {
			assert false : iae.toString();
			return null;
		}
	}
	
	
	/* listener functionality */
	
	public void add( IGodListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	public void remove( IGodListener listener) {
		// TODO Auto-generated method stub
		listeners.remove(listener);
	}
	
	/* private methods */
	
	private void notifyListeners() {
		if ( !listeners.isEmpty() ) {
			 Object[] all = listeners.getListeners();
			 for (int i = 0; i < all.length; ++i) {
			    ((IGodListener)all[i]).modifiedEvent();
			 }
		}
	}

	private God() {
		super();
		addClass( Person.class );
		addClass( Dog.class );
		// TODO Auto-generated constructor stub
	}

}
