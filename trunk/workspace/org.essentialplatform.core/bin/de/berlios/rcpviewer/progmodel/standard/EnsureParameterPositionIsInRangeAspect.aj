/**
 * 
 */
package org.essentialplatform.progmodel.standard;

import org.eclipse.emf.ecore.EOperation;

/**
 * Cross-cutting validation of any convenience methods on operations that
 * take a second int (being the parameter position). 
 * 
* <p>
* TODO: this should be refactored as a private static nested aspect of
* DomainClass - awaiting complete refactoring support in AJDT/JDT.
 */
aspect EnsureParameterPositionIsInRangeAspect {

	declare precedence: EnsureMemberBelongsToDomainClassAspect, 
						EnsureParameterPositionIsInRangeAspect;

	pointcut convenienceMethodForParameter(EOperation eOperation, int parameterPosition):
		execution(* *..DomainClass+.*(EOperation, int)) &&
		args(eOperation, parameterPosition);
	
	before(EOperation eOperation, int parameterPosition): 
		convenienceMethodForParameter(eOperation, parameterPosition) {
		int numberOfParametersForOperation = eOperation.getEParameters().size();  
		if (numberOfParametersForOperation == 0) {
			throw new IllegalArgumentException(
				"EOperation '" + eOperation + "' takes no parameters");
		}
		if (parameterPosition < 0 ||
		    parameterPosition >= numberOfParametersForOperation) {
			throw new IllegalArgumentException(
				"EOperation '" + eOperation + "': 0 <= parameterPosition < " + numberOfParametersForOperation);
		}
	}
}
