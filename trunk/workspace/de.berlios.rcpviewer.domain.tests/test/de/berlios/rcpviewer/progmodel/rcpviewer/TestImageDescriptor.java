package de.berlios.rcpviewer.progmodel.rcpviewer;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDomainSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>ImageUrlAt</tt> annotations.
 *
 * <p>
 * TODO: should fix the URL ref.
 * 
 * @author Dan Haywood
 */
public abstract class TestImageDescriptor extends AbstractTestCase {

	public TestImageDescriptor(IDomainSpecifics domainSpecifics, IDomainAnalyzer domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}


	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithImageUrlAt() {
		domainClass = lookupAny(ProspectiveSale.class);
		getDomainInstance().addExtension(getDomainAnalyzer());
		getDomainInstance().done();
		
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNotNull(id);
	}

	/**
	 * currently failing: still returning an ImageDescriptor even if no extension installed ...
	 *
	 */
	public void incompletetestDomainClassWithImageUrlAtWithoutExtensionInstalled() {
		domainClass = lookupAny(ProspectiveSale.class);
		Domain.instance().done();
		
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
