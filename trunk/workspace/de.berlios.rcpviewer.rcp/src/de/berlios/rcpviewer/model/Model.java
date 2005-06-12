package de.berlios.rcpviewer.model;

import java.util.ArrayList;
import java.util.List;

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
	
	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#getObjects()
	 */
	public Object[] getObjects() {
		return objects.toArray();
	}
	
	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#addObject(java.lang.Object)
	 */
	public boolean addObject( Object obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( objects.contains( obj ) ) return false;
		if ( objects.add( obj ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#removeObject(java.lang.Object)
	 */
	public boolean removeObject( Object obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( !objects.contains( obj ) ) return false;
		if ( objects.remove( obj ) ) {
			notifyListeners();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#reset()
	 */
	public void reset() {
		objects.clear();
		notifyListeners();
	}
	
	
	/* listener functionality */
	
	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#add(mikespike3.model.IModelListener)
	 */
	public void add( IModelListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see mikespike3.model.IModel#remove(mikespike3.model.IModelListener)
	 */
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
	}

}
