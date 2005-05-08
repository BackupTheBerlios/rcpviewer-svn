package de.berlios.rcpviewer.progmodel.rcpviewer;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

/**
 * Tests for the use of the <tt>ImageUrlAt</tt> annotations.
 *
 * <p>
 * TODO: should fix the URL ref.
 * 
 * @author Dan Haywood
 */
public class TestImageDescriptor extends AbstractTestCase {

    @ImageUrlAt("http://www.eclipse.org/artwork/builtoneclipse/images/bui_eclipse_pos_logo_fc_sm.jpg")
	public static class ProspectiveSale {
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		MetaModel.instance().clear();
		super.tearDown();
	}
	
	public void testDomainClassWithImageUrlAt() {
		domainClass = MetaModel.instance().register(ProspectiveSale.class);
		MetaModel.instance().addExtension(new RcpViewerExtension());
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNotNull(id);
	}

	/**
	 * currently failing: still returning an ImageDescriptor even if no extension installed ...
	 *
	 */
	public void incompletetestDomainClassWithImageUrlAtWithoutExtensionInstalled() {
		domainClass = new DomainClass<ProspectiveSale>(ProspectiveSale.class);
		ImageDescriptor id = (ImageDescriptor)domainClass.getAdapter(ImageDescriptor.class);
		assertNull(id);
	}

	
}
