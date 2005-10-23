package de.berlios.rcpviewer.gui.acme.model;

import de.berlios.rcpviewer.progmodel.extended.Lifecycle;

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
