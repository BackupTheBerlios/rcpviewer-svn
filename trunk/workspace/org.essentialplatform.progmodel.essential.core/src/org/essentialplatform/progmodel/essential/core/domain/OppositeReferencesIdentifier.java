package org.essentialplatform.progmodel.essential.core.domain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import org.essentialplatform.core.domain.DomainClass;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.DomainClass.OppRefState;
import org.essentialplatform.progmodel.essential.app.OppositeOf;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;

/**
 * This is a horrible hack; called both by {@link org.essentialplatform.progmodel.standard.StandardProgModelDomainBuilder} 
 * and also {@link org.essentialplatform.core.domain.Domain}. 
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
		
		for(IDomainClass.IReference iReference: domainClass.iReferences()) {
			EReference eReference = iReference.getEReference();
			// since later on we call this same method on the DomainClass 
			// representing the referenced class, we have the possibility of
			// an infinite loop if both sides have an @OppositeOf annotation.
			// this guard prevents this from happening.
			if (eReference.getEOpposite() != null) {
				continue;
			}
			EClass referencedEClass = eReference.getEReferenceType();
			IDomainClass referencedClass = 
				domainClass.getDomain().domainClassFor(referencedEClass);

			OppositeOf oppositeOf = 
				serializer.getReferenceOppositeOf(eReference);
			if (oppositeOf != null) {
				// annotation on this end, so set up the opposite
				IDomainClass.IReference oppositeIReference = 
					referencedClass.getIReferenceNamed(oppositeOf.value());
				if(oppositeIReference != null) {
					EReference oppositeEReference = oppositeIReference.getEReference();
					eReference.setEOpposite(oppositeEReference);
					oppositeEReference.setEOpposite(eReference);
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
