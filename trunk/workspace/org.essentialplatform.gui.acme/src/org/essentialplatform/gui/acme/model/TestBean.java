package org.essentialplatform.gui.acme.model;

import org.essentialplatform.progmodel.essential.app.Lifecycle;

/**
 * Test
 * @author Mike
 */
@Lifecycle(instantiable=true,searchable=true,saveable=true)
public class TestBean {	
	
	private EasyBean _easyBean = null;

	/**
	 * @return Returns the easybean.
	 */
	public EasyBean getEasyBean() {
		return _easyBean;
	}
	
	// can add
	public void associateEasyBean(EasyBean easyBean) {
		_easyBean = easyBean;
	}
	// can remove
	public void dissociateEasyBean(EasyBean easyBean) {
		_easyBean = null;
	}
	
}
