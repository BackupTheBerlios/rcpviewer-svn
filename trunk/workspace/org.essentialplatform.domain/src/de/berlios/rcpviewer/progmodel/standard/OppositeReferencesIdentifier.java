package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.DomainClass;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.DomainClass.OppRefState;

/**
 * This is a horrible hack; called both by {@link de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder} 
 * and also {@link de.berlios.rcpviewer.domain.Domain}. 
 * 
 * @author Dan Haywood
 */
public class OppositeReferencesIdentifier {

	private EssentialProgModelStandardSemanticsEmfSerializer serializer = new EssentialProgModelStandardSemanticsEmfSerializer();

	private final DomainClass domainClass;
	public OppositeReferencesIdentifier(DomainClass runtimeDomainClass) {
		this.domainClass = runtimeDomainClass;
	}
	public void identify() {
		if (domainClass.oppRefState == OppRefState.neverAgain) {
			return;
		} else if (domainClass.oppRefState == OppRefState.stillBuilding) {
			// do nothing
		} else if (domainClass.oppRefState == OppRefState.onceMore) {
			domainClass.oppRefState = OppRefState.neverAgain;
		}
		
		for(EReference reference: domainClass.references()) {
			// since later on we call this same method on the DomainClass 
			// representing the referenced class, we have the possibility of
			// an infinite loop if both sides have an @OppositeOf annotation.
			// this guard prevents this from happening.
			if (reference.getEOpposite() != null) {
				continue;
			}
			EClass referencedEClass = reference.getEReferenceType();
			IDomainClass referencedClass = 
				domainClass.getDomain().domainClassFor(referencedEClass);

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
