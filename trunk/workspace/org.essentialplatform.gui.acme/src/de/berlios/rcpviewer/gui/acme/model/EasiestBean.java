package de.berlios.rcpviewer.gui.acme.model;

import org.essentialplatform.louis.util.RandomUtil;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
@InDomain
public class EasiestBean {
	
	private String field0 = null;
	

	public EasiestBean() {
		field0 = RandomUtil.createString( 0, 10 );
	}

	String getField0() {
		return field0;
	}

	void setField0(String field0) {
		this.field0 = field0;
	}
	
	
}
