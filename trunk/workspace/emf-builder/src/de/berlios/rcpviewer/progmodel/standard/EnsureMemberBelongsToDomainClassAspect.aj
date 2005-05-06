package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EOperationImpl;
import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.progmodel.standard.Constants;
import de.berlios.rcpviewer.metamodel.MetaModel;
import de.berlios.rcpviewer.metamodel.EmfFacade;
import de.berlios.rcpviewer.metamodel.EmfFacadeAware;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.metamodel.II18nData;
import de.berlios.rcpviewer.metamodel.OperationKind;
import de.berlios.rcpviewer.metamodel.MethodNameHelper;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;
import de.berlios.rcpviewer.progmodel.standard.Constants;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

/**
* Cross-cutting validation of any eAttributes or eOperations 
* passed to us in the various convenience methods.
*
* <p>
* TODO: this should be refactored as a private static nested aspect of
* DomainClass - awaiting complete refactoring support in AJDT/JDT.
*/
aspect EnsureMemberBelongsToDomainClassAspect {

	pointcut convenienceMethodForAttribute(DomainClass domainClass, EAttribute eAttribute):
		execution(* *..DomainClass+.*(EAttribute, ..)) &&
		!execution(* *..DomainClass+.containsAttribute(EAttribute)) &&
		this(domainClass) && args(eAttribute, ..);
	
	before(DomainClass domainClass, EAttribute eAttribute): 
		convenienceMethodForAttribute(domainClass, eAttribute) {
		if (domainClass.containsAttribute(eAttribute)) {
			return;
		}
		throw new IllegalArgumentException(
			"EAttribute '" + eAttribute + "' not part of this DomainClass");
	}

	pointcut convenienceMethodForOperation(DomainClass domainClass, EOperation eOperation):
		execution(* *..DomainClass+.*(EOperation, ..)) &&
		!execution(* *..DomainClass+.containsOperation(EOperation)) &&
		this(domainClass) && args(eOperation, ..);
	
	before(DomainClass domainClass, EOperation eOperation): 
		convenienceMethodForOperation(domainClass, eOperation) {
		if (domainClass.containsOperation(eOperation)) {
			return;
		}
		throw new IllegalArgumentException(
			"EOperation '" + eOperation + "' not part of this DomainClass");
	}

}
