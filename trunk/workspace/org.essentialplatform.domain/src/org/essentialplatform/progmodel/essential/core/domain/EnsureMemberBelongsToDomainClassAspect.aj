package org.essentialplatform.progmodel.essential.core.domain;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

/**
* Cross-cutting validation of any eAttributes or eOperations 
* passed to us in the various convenience methods.
*
* <p>
* TODO: this should be refactored as a private static nested aspect of
* DomainClass - awaiting complete refactoring support in AJDT/JDT.
*/
aspect EnsureMemberBelongsToDomainClassAspect {

//	pointcut convenienceMethodForAttribute(DomainClass domainClass, EAttribute eAttribute):
//		execution(* *..DomainClass+.*(EAttribute, ..)) &&
//		!execution(* *..DomainClass+.containsAttribute(EAttribute)) &&
//		this(domainClass) && args(eAttribute, ..);
//	
//	before(DomainClass domainClass, EAttribute eAttribute): 
//		convenienceMethodForAttribute(domainClass, eAttribute) {
//		if (domainClass.containsAttribute(eAttribute)) {
//			return;
//		}
//		throw new IllegalArgumentException(
//			"EAttribute '" + eAttribute + "' not part of this DomainClass");
//	}
//
//	pointcut convenienceMethodForOperation(DomainClass domainClass, EOperation eOperation):
//		execution(* *..DomainClass+.*(EOperation, ..)) &&
//		!execution(* *..DomainClass+.containsOperation(EOperation)) &&
//		this(domainClass) && args(eOperation, ..);
//	
//	before(DomainClass domainClass, EOperation eOperation): 
//		convenienceMethodForOperation(domainClass, eOperation) {
//		if (domainClass.containsOperation(eOperation)) {
//			return;
//		}
//		throw new IllegalArgumentException(
//			"EOperation '" + eOperation + "' not part of this DomainClass");
//	}
//
//
//	pointcut convenienceMethodForReference(DomainClass domainClass, EReference eReference):
//		execution(* *..DomainClass+.*(EReference, ..)) &&
//		!execution(* *..DomainClass+.containsReference(EReference)) &&
//		this(domainClass) && args(eReference, ..);
//	
//	before(DomainClass domainClass, EReference eReference): 
//		convenienceMethodForReference(domainClass, eReference) {
//		if (domainClass.containsReference(eReference)) {
//			return;
//		}
//		throw new IllegalArgumentException(
//			"EReference '" + eReference+ "' not part of this DomainClass");
//	}
}
