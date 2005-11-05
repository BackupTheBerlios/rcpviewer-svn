package org.essentialplatform.gui.acme.model;

import org.essentialplatform.louis.util.RandomUtil;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;

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
