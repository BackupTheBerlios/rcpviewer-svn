/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package mikespike3.application;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the PrimerPO editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class EasyBeanPlugin extends AbstractUIPlugin {

	static EasyBeanPlugin __instance;
	
	public static EasyBeanPlugin getInstance() {
		return __instance;
	}
	
	@Override
	public void start(BundleContext pContext) throws Exception {
		super.start(pContext);
		__instance= this;
	}
	
	
}
