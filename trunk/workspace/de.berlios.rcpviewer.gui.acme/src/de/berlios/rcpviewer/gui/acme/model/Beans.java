package de.berlios.rcpviewer.gui.acme.model;

import java.util.ArrayList;
import java.util.Collection;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;

/**
 * Stores many beans
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class Beans {

	private final Collection<EasyBean> _easybeans = new ArrayList<EasyBean>();
	private int numBeans = 0;
	
	@TypeOf( EasyBean.class )
	public Collection<EasyBean> getEasyBeans() {
		return _easybeans;
	}
	
	public void addEasyBean( EasyBean bean ) {
		if ( bean == null ) throw new IllegalArgumentException();
		_easybeans.add( bean );
		numBeans = _easybeans.size();
	}
	
	public void removeEasyBean( EasyBean bean ) {
		if ( bean == null ) throw new IllegalArgumentException();
		_easybeans.remove( bean );
		numBeans = _easybeans.size();
	}
	
	public int getNumBeans() {
		return numBeans;
	}
}
