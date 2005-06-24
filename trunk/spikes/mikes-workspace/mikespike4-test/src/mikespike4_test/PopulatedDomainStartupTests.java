package mikespike4_test;

import java.util.Collection;
import java.util.List;

import mikespike4.GuiPlugin;
import mikespike4.rcp.ApplicationWorkbenchWindowAdvisor;
import mikespike4.views.classbar.ClassBarView;
import mikespike4_test.util.GuiReadUtil;

import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import de.berlios.rcpviewer.domain.Department;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Thes are tests to try out the initialisation of the GUI with a populated 
 * domain.
 * @author Mike
 *
 */
public class PopulatedDomainStartupTests extends AbstractRcpTestCase {
	

	@Override
	protected void setUp() throws Exception {
		Domain.lookupAny( Department.class );
		Domain.instance().done();
		super.setUp();
	}

	/**
	 * Test that the workbench opens.
	 */
	public void testWorkbenchOpened() {
		assertNotNull( GuiReadUtil.getActivePage() );
	}
	
	/**
	 * Test that resources can be read - use workbench title for this
	 */
	public void testWorkbenchTitle() {
		assertEquals(
				GuiPlugin.getResourceString(
						ApplicationWorkbenchWindowAdvisor.TITLE_KEY  ),
				PlatformUI.getWorkbench()
				          .getActiveWorkbenchWindow()
				          .getShell()
				          .getText() );
	}	
	
	/**
	 * Test that class bar instantiated
	 */
	public void testClassBarExists() {
		assertNotNull( GuiReadUtil.getViewPart( ClassBarView.ID ) );
	}	

	/**
	 * Test that class bar displays each domian class
	 */
	public void testClassBarContents() {
		// get view contents
		IViewPart view = GuiReadUtil.getViewPart( ClassBarView.ID ) ;
		assertTrue( view instanceof ClassBarView );
		List<Button> buttons = ((ClassBarView)view).testGetButtons();
		
		// get domain contents
		Collection<IDomainClass<?>> classes = Domain.instance().classes();
		
		// compare..
		assertEquals( classes.size(), buttons.size() );
		
	}
}
