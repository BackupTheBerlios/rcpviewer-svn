package mikespike1_test;

import junit.framework.TestCase;
import mikespike1_runtime.View;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewPart;

public class Test extends TestCase {
	
	/**
	 * First dummy test.
	 */
	public void testDummy() {
		// does nowt
	}
	
	/**
	 * Proves it works!
	 */
//	public void testThisShouldFail() {
//		fail( "JUnit working!");
//	}

	/**
	 * Duh!
	 */
	public void testWorkbenchOpened() {
		assertNotNull( TestUtil.getActivePage() );
	}
	
	/**
	 * Less duh!
	 */
	public void testViewOpened() {
		assertNull( TestUtil.getViewPart( "wibble ") );
		assertNotNull( TestUtil.getViewPart( View.ID ) );
	}
	
	/**
	 * Note how this and all subsequent tests are tied to the implementation.
	 */
	public void testAccessViewer() {
		IViewPart view = TestUtil.getViewPart( View.ID );
		Object viewer = TestUtil.getField( view, "viewer" );
		assertNotNull( viewer );
		assert( viewer instanceof TableViewer );
	}
	
	/**
	 * Note how we ignore all model, input providers etc and go straight to 
	 * the visual output of the table itself.
	 */
	public void testViewerContents() {
		TableViewer viewer = (TableViewer)TestUtil.getField( 
				TestUtil.getViewPart( View.ID ), 
				"viewer" );
		Table table = viewer.getTable();
		
		String[] expected = new String[] { "One", "Two", "Three" };
		assertEquals( table.getItemCount(), expected.length );
		for ( int i=0, num = expected.length; i < num ; i++ ) {
			assertEquals( table.getItem(i).getData(), expected[i] );
		}
	}
}
