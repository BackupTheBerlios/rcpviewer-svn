package org.essentialplatform.runtime.shared.domain;

/**
 * Enforces rules specific to the Essential Programming Model.
 * 
 * <p>
 * The idea here is to ensure we only do add or remove from collections
 * from within addTo or removeFrom methods.
 */
public aspect EssentialProgModelRulesAspect {

	declare error: 
		call(public boolean java.util.Collection+.add(Object+)) &&
		withincode(* IPojo+.*(..)) &&
		!withincode(void IPojo+.addTo*(..)):
			"can only add to collection from within an addto method"; 

	declare error: 
		call(public boolean java.util.Collection+.remove(Object+)) &&
		withincode(* IPojo+.*(..)) &&
		!withincode(void IPojo+.removeFrom*(..)):
			"can remove from collection from within an addto method"; 


}
