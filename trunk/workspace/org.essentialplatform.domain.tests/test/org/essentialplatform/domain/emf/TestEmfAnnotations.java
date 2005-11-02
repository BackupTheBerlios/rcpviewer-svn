package org.essentialplatform.domain.emf;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.essentialplatform.domain.Emf;
import org.essentialplatform.domain.EmfAnnotations;

/**
 * Independent (so also applicable) to both runtime and compiletime, 
 * and therefore tested in both.
 * 
 * @author Dan Haywood
 */
public class TestEmfAnnotations extends TestCase {

	private EmfAnnotations _emfAnnotations;
	
	protected void setUp() throws Exception {
		super.setUp();
		_emfAnnotations = new EmfAnnotations();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		_emfAnnotations = null;
	}
	
	
	
	/**
	 * Obtain annotation of supplied source on a model element.
	 */
	public void testAnnotationOf() {
		// any model element should do
		EModelElement modelElement = new Emf().getEcorePackage();
		EAnnotation eAnnotation = _emfAnnotations.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		assertNotNull(eAnnotation);
		assertEquals("http://rcpviewer.berlios.de/test/source", eAnnotation.getSource());
	}

	/**
	 * The EAnnotations#details is a map-like construct that can be put to
	 * and got from.
	 * 
	 *  TODO: marked incomplete because under JUnit plugin test the first assert fails.
	 */
	public void incompletetestSetEAnnotationsDetails() {
		// any model element should do
		EModelElement modelElement = new Emf().getEcorePackage();
		EAnnotation eAnnotation = _emfAnnotations.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		Map<String,String> details = new HashMap<String,String>();
		details.put("foo", "bar");
		details.put("baz", "boz");
		assertEquals(0, eAnnotation.getDetails().size());
		_emfAnnotations.putAnnotationDetails(eAnnotation, details);
		assertEquals(2, eAnnotation.getDetails().size());
	}

	/**
	 * The EAnnotations#details is a map-like construct that can be put to
	 * and got from. 
	 */
	public void testGetEAnnotationsDetails() {
		// any model element should do
		EModelElement modelElement = new Emf().getEcorePackage();
		EAnnotation eAnnotation = _emfAnnotations.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		Map<String,String> details = new HashMap<String,String>();
		details.put("foo", "bar");
		details.put("baz", "boz");
		_emfAnnotations.putAnnotationDetails(eAnnotation, details);
		
		Map<String,String> retrievedDetails = _emfAnnotations.getAnnotationDetails(eAnnotation);
		assertEquals(2, retrievedDetails.size());
		assertEquals("bar", retrievedDetails.get("foo"));
		assertEquals("boz", retrievedDetails.get("baz"));
	}

}
