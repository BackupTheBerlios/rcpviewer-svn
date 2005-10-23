package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.RuntimeDomainClass.OppRefState;

/**
 * This is a horrible hack; called both by {@link de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder} 
 * and also {@link de.berlios.rcpviewer.domain.RuntimeDomain}. 
 * 
 * @author Dan Haywood
 */
public class OppositeReferencesIdentifier<T> {

	private StandardProgModelSemanticsEmfSerializer serializer = new StandardProgModelSemanticsEmfSerializer();

	private final RuntimeDomainClass<T> runtimeDomainClass;
	public OppositeReferencesIdentifier(RuntimeDomainClass<T> runtimeDomainClass) {
		this.runtimeDomainClass = runtimeDomainClass;
	}
	public void identify() {
		if (runtimeDomainClass.oppRefState == OppRefState.neverAgain) {
			return;
		} else if (runtimeDomainClass.oppRefState == OppRefState.stillBuilding) {
			// do nothing
		} else if (runtimeDomainClass.oppRefState == OppRefState.onceMore) {
			runtimeDomainClass.oppRefState = OppRefState.neverAgain;
		}
		
		for(EReference reference: runtimeDomainClass.references()) {
			// since later on we call this same method on the DomainClass 
			// representing the referenced class, we have the possibility of
			// an infinite loop if both sides have an @OppositeOf annotation.
			// this guard prevents this from happening.
			if (reference.getEOpposite() != null) {
				continue;
			}
			EClass referencedEClass = reference.getEReferenceType();
			IDomainClass<?> referencedClass = 
				runtimeDomainClass.getDomain().domainClassFor(referencedEClass);

			OppositeOf oppositeOf = 
				serializer.getReferenceOppositeOf(reference);
			if (oppositeOf != null) {
				// annotation on this end, so set up the opposite
				EReference oppositeReference = 
					referencedClass.getEReferenceNamed(oppositeOf.value());
				if(oppositeReference != null) {
					reference.setEOpposite(oppositeReference);
					oppositeReference.setEOpposite(reference);
				}
			} else {
				// no annotation this end, but its possible that the referenced
				// object does have an annotation its end.
				// however, we pick this up by doing postprocessubg when we
				// do a lookup of any object.
			}
		}
		
	}
	
	

}
