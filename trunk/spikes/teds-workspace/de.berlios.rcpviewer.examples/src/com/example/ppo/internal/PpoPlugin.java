/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.example.ppo.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the PrimerPO editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class PpoPlugin extends AbstractUIPlugin {

	static PpoPlugin __instance;
	
	public static PpoPlugin getInstance() {
		return __instance;
	}
	
	@Override
	public void start(BundleContext pContext) throws Exception {
		super.start(pContext);
		__instance= this;
	}
	
	
}
