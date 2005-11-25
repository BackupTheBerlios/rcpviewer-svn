/**
 * 
 */
package org.essentialplatform.louis.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Applies a boolean OR to the results of <code>select</code> on its
 * delegates.  If no delegates set returns <code>false</code>
 * @author Mike
 *
 */
public class BooleanORFilter extends ViewerFilter {

    private final List<ViewerFilter> _delegates;

	/**
	 * No arg constructor.
	 */
	public BooleanORFilter() {
		_delegates = new ArrayList<ViewerFilter>();
	}

	/**
     * Applies a boolean OR to the results of <code>select</code> on its
     * delegates.  If no delegates set returns <code>false</code>
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
        for ( ViewerFilter filter : _delegates ) {
        	if ( filter.select( viewer, parentElement, element ) ) {
        		return true;
            }
        }
        return false;
	}
    
    /**
     * Add a delegate filter
     * @param filter
     */
    public void addDelegate( ViewerFilter filter ) {
    	if ( filter == null ) throw new IllegalArgumentException();
        _delegates.add( filter );
    }
    
    /**
     * Clear all delegates
     */
    public void reset() {
    	_delegates.clear();
    }
    
    /**
     * Return is any delegates have been added.
     * @return
     */
    public boolean isEmpty() {
    	return _delegates.isEmpty();
    }

}

