package org.essentialplatform.runtime.transaction.internal;

import org.aspectj.lang.Signature;
import org.eclipse.emf.ecore.EAttribute;
import org.aspectj.lang.JoinPoint;

import org.essentialplatform.runtime.session.IPersistable.PersistState;
import org.essentialplatform.runtime.session.*;

import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * Contents has been (temporarily) copied into TransactionAttributeChangeAspect
 * since (for some reason) doesn't seem to be being picked up.
 * 
 * <p>
 * Problem initially started when changed package structures.
 */
public aspect NotifyListenersAspect extends PojoAspect {


}
