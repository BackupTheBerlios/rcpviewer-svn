package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * Enforces various rules at compile-time.
 * 
 * @author Dan Haywood
 */
aspect CompileTimeRulesAspect {

	declare error: 
		staticinitialization(@Value *) &&
		staticinitialization(@Domain *): 
			"Cannot be annotated as both @Value and @Domain";

}
