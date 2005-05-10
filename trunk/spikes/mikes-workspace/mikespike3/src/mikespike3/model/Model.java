package mikespike3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mikespike3.util.RandomUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.ListenerList;

/**
 * Anitpattern representing the metamodel.
 * @author Mike
 *
 */
public class Model {
	
	/* statics */
	
	private static Model instance = null;
	
	// synchronising indicates a lack of belief in there being only one god
	public static final synchronized Model getInstance() {
		if ( instance == null ) {
			instance = new Model();
		}
		return instance;
	}
	
	
	/* fields */
	
	// want unique list - why not a set? - want a fixed order
	private final List objects = new ArrayList();
	
	// a jface class so no really appropriate for a model class!
	private final ListenerList listeners = new ListenerList();
	
	
	/* metamodel contract */
	
	public Object[] getObjects() {
		return objects.toArray();
	}
	
	public boolean addObject( Object obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( objects.contains( obj ) ) return false;
		if ( objects.add( obj ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public boolean removeObject( Object obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( !objects.contains( obj ) ) return false;
		if ( objects.remove( obj ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public void reset() {
		objects.clear();
		notifyListeners();
	}
	
	
	/* listener functionality */
	
	public void add( IModelListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	public void remove( IModelListener listener) {
		// TODO Auto-generated method stub
		listeners.remove(listener);
	}
	
	/* private methods */
	
	private void notifyListeners() {
		if ( !listeners.isEmpty() ) {
			 Object[] all = listeners.getListeners();
			 for (int i = 0; i < all.length; ++i) {
			    ((IModelListener)all[i]).modifiedEvent();
			 }
		}
	}

	private Model() {
		// auto-populate?
		String[] args = Platform.getApplicationArgs();
		Arrays.sort( args );
		if ( Arrays.binarySearch( args, "-test" ) < 0 ) {
			for ( int i=0 ; i < RandomUtil.createInt( 5, 20 ); i++ ) {
				addObject( new EasyBean() );
			}
		}
	}

}
