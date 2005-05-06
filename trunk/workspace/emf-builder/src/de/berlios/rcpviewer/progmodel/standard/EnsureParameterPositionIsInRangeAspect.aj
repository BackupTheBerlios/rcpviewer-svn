/**
 * 
 */
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
