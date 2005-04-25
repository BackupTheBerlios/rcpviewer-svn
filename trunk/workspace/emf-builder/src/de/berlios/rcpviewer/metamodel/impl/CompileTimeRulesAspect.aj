package de.berlios.rcpviewer.metamodel.impl;
import de.berlios.rcpviewer.metamodel.annotations.*;

/**
 * Enforces various rules at compile-time. 
 */
aspect CompileTimeRulesAspect {

	declare error: 
		staticinitialization(@Value *) &&
		staticinitialization(@Domain *): 
			"Cannot be annotated as both @Value and @Domain";

}
