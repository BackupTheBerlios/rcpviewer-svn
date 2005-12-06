package org.essentialplatform.gui.acme.model;

import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.TypeOf;

/**
 * Stores many beans
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class Beans {

	public int getNumBeans() {
		return numBeans;
	}
	private int numBeans = 0;

	
	@TypeOf( EasyBean.class )
	public Set<EasyBean> getEasyBeans() {
		return easyBeans;
	}
	public void addToEasyBeans( EasyBean easyBean ) {
		if ( easyBean == null ) throw new IllegalArgumentException();
		easyBeans.add( easyBean );
		numBeans = easyBeans.size();
	}
	public void removeFromEasyBeans( EasyBean easyBean ) {
		if ( easyBean == null ) throw new IllegalArgumentException();
		easyBeans.remove( easyBean );
		numBeans = easyBeans.size();
	}
	private final Set<EasyBean> easyBeans = new HashSet<EasyBean>();
	

	/**
	 * @return Returns the easybean.
	 */
	public EasyBean getEasyBean() {
		return easyBean;
	}
	/**
	 * @param easyBean The easybean to set.
	 */
	public void setEasyBean(EasyBean easyBean) {
		this.easyBean = easyBean;
	}
	// can add
	public void associateEasyBean(EasyBean easyBean) {
		setEasyBean(easyBean);
	}
	// can remove
	public void dissociateEasyBean(EasyBean easyBean) {
		setEasyBean(null);
	}
	private EasyBean easyBean = null;
	
}
