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
		return easybeans;
	}
	public void addToEasyBeans( EasyBean bean ) {
		if ( bean == null ) throw new IllegalArgumentException();
		easybeans.add( bean );
		numBeans = easybeans.size();
	}
	public void removeFromEasyBeans( EasyBean bean ) {
		if ( bean == null ) throw new IllegalArgumentException();
		easybeans.remove( bean );
		numBeans = easybeans.size();
	}
	private final Set<EasyBean> easybeans = new HashSet<EasyBean>();
	

	/**
	 * @return Returns the easybean.
	 */
	public EasyBean getEasyBean() {
		return easybean;
	}
	/**
	 * @param easybean The easybean to set.
	 */
	public void setEasyBean(EasyBean easybean) {
		this.easybean = easybean;
	}
	// can add
	public void associateEasyBean(EasyBean easyBean) {
		setEasyBean(easyBean);
	}
	// can remove
	public void dissociateEasyBean(EasyBean easyBean) {
		setEasyBean(null);
	}
	private EasyBean easybean = null;
	
}
