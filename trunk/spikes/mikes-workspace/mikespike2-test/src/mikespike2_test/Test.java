package mikespike2_test;

import java.util.Arrays;

import junit.framework.TestCase;
import mikespike2_runtime.model.Dog;
import mikespike2_runtime.model.God;
import mikespike2_runtime.model.Person;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewPart;

public class Test extends TestCase {
	

	/**
	 * 1.
	 * Test that the wizard has produced a workbench that opens.  (This is the
	 * only test that does not precede its implementation).
	 */
	public void testWorkbenchOpened() {
		assertNotNull( TestUtil.getActivePage() );
	}
	
	/**
	 * 2.
	 * REQUIREMENT : the app must immediately open a view that we will use to 
	 * display all possible domain classes.
	 * TEST : a view with id 'mikespike2_runtime.ClassExplorerView' (eerily
	 * similar to a class name) must automatically be opened. 
	 * IMPLEMENTATION : 
	 * 1. created subclass of ViewPart 'mikespike2_runtime.ClassExplorerView'
	 * 2. defined it as an 'org.eclipse.ui.views' extension point in plugin.xml 
	 * 3. added it to the default perspective createInitialLayout method
	 * 4. debug until test works
	 * ISSUES : Should I test location etc.?  Umm... nah. 
	 */
	public void testViewCreated() {
		assertNotNull( TestUtil.getViewPart( "mikespike2_runtime.ClassExplorerView" ));
	}
	
	/**
	 * 3.
	 * REQUIREMENT : the ClassExplorer view must list all classes in
	 * the domain model.
	 * ISSUES (1) : What domain model?  OK we need one.  However this spike is about
	 * test-driven GUI development rather than metamodelling so what is the
	 * simplist substitute?  Well almost always the answer for immediacy and
	 * simplicity within a single VM is the 'God' antipattern - a singleton that
	 * knows everything.  That'll do for now.
	 * TEST : check singleton instance exists
	 * IMPLEMENTATION :
	 * 1. Create singleton.
	 * 2. debug until test works
	 * ISSUES (2) : This failed initially with a NoClassDef error.  Typical
	 * Eclipse PDE issue - the mikespike2-runtime packages were on the compile 
	 * path but not exported at runtime by the parent plugin - have to add them
	 * to 'Exported Packages' on the mikespike2-runtime plugin.xml.  Just proves
	 * the utility of very basic tests.
	 */
	public void testForGodsExistence() {
		// fairly philisophical eh?
		assertNotNull( God.getInstance() );
	}
	
	/**
	 * 4.
	 * REQUIREMENT : need God to present a domain model.
	 * ISSUES : Let's make it dynamic and be able to supply minimal metadata so
	 * lets have:
	 * For God:
	 * 1. Class[] getClasses(); (defined as starting as an empty array)
	 * 2. boolean addClass( Class );
	 * 3. boolean removeClass( Class );
	 * 4. String getName( Class ); (defined as class name without package)
	 * 5. additional sematics - classes must be unique, nulls guarded by 
	 * IllegalArgumentExceptions, order is preserved
	 * For Model:
	 * 1. Person
	 * 2. Dog
	 * TEST : a few basic tests (as the metamodel is not the focus of this spike)
	 * IMPLEMENTATION :
	 * 1. debug until test works
	 */
	public void testBasicMetaModel() {
		assertEquals( 0, God.getInstance().getClasses().length  );
		
		assertTrue( God.getInstance().addClass( Person.class ) ) ;
		assertFalse( God.getInstance().addClass( Person.class ) ) ;
		assertTrue( Arrays.equals(
				new Class[]{ Person.class }, 
				God.getInstance().getClasses() ) );
		
		assertTrue( God.getInstance().addClass( Dog.class ) ) ;
		assertFalse( God.getInstance().addClass( Dog.class ) ) ;
		assertTrue( Arrays.equals(
				new Class[]{ Person.class, Dog.class }, 
				God.getInstance().getClasses() ) );
		
		assertEquals( "Person", God.getInstance().getName( Person.class) );
		assertEquals( "Dog", God.getInstance().getName( Dog.class) );
		
		assertTrue( God.getInstance().removeClass( Person.class ) ) ;
		assertFalse( God.getInstance().removeClass( Person.class ) ) ;
		assertTrue( Arrays.equals(
				 new Class[]{ Dog.class }, 
			     God.getInstance().getClasses() )  );	
		
		try {
			assertEquals( "Person", God.getInstance().getName( Person.class) );
			fail ( "should be an IlleaglArgumentException");
		}
		catch ( IllegalArgumentException iae ) {
			// success!
		}
		assertEquals( "Dog", God.getInstance().getName( Dog.class) );
	}
	
	/**
	 * 5:
	 * REQUIREMENT - as 3: the ClassExplorer view must list all classes in
	 * the domain model. 
	 * TEST : empty God means as empty viewer 
	 * IMPLEMENTATION :
	 * 1. add a table viewer named 'viewer' to the ClassExplorerView with 
	 * a ContentProvider and a LabelProvider that extract name from God.
	 * 2. debug until test works
	 * ISSUES : dratted singleton's - tests fail because God retains state from
	 * previous state - added reset() method to God and overrode the setUp()
	 * method of this Test class to call this.
	 */
	public void testNoClassesDisplay() {
		assertEquals( 0, God.getInstance().getClasses().length  );
		
		IViewPart view = TestUtil.getViewPart( 
				"mikespike2_runtime.ClassExplorerView" );
		Object viewer = TestUtil.getField( view, "viewer" );
		assertNotNull( viewer );
		assertTrue( viewer instanceof TableViewer );
		assertEquals( 0, ((TableViewer)viewer).getTable().getItemCount() );
	}
	
	

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		God.getInstance().reset();
		assertEquals( 0, God.getInstance().getClasses().length  );
	}
	
	/**
	 * 6 : 
	 * REQUIREMENT - as 3: the ClassExplorer view must list all classes in
	 * the domain model.
	 * TEST : as 5: but test with God populated with a single class
	 * ISSUES : Initially fails because the viewer is populated before the
	 * test adds Person to God.  We need the viewer to re-populate whenever God 
	 * changes.  Solution - add a listening mechanism based on a new
	 * IGodListener interface:
	 * 1. added listening related functionality to God
	 * 2. created listener on Viewer creation - note a DisposeListener should
	 * ALWAYS be added
	 */
	public void testClassDisplay() {
		
		God.getInstance().addClass( Person.class );
		
		// note : have extracted the code to access the viewer to a 
		// seperate method as used repeatedly from now on
		TableItem[] items = getClassExplorer().getTable().getItems();
		assertEquals( 1, items.length );
		assertEquals( Person.class, items[0].getData() ) ;
		assertEquals( "Person", items[0].getText() );
	}
	
	private TableViewer getClassExplorer() {
		IViewPart view = TestUtil.getViewPart( 
				"mikespike2_runtime.ClassExplorerView" );
		Object viewer = TestUtil.getField( view, "viewer" );
		assertNotNull( viewer );
		assertTrue( viewer instanceof TableViewer );
		return (TableViewer)viewer;
	}
	
	/**
	 * 7:
	 * REQUIREMENT - as 3: the ClassExplorer view must list all classes in
	 * the domain model.
	 * TEST : as 6: but test with God's population dynamically changed
	 * ISSUES : note also testing that order is preserved
	 */
	public void testClassesDisplay() {
		
		God.getInstance().addClass( Person.class );
		God.getInstance().addClass( Dog.class );
		TableItem[] items = getClassExplorer().getTable().getItems();
		assertEquals( 2, items.length );
		assertEquals( Person.class, items[0].getData() ) ;
		assertEquals( "Person", items[0].getText() );
		assertEquals( Dog.class, items[1].getData() ) ;
		assertEquals( "Dog", items[1].getText() );
		
		God.getInstance().removeClass( Person.class );
		items = getClassExplorer().getTable().getItems();
		assertEquals( 1, items.length );
		assertEquals( Dog.class, items[0].getData() ) ;
		assertEquals( "Dog", items[0].getText() );
		
		God.getInstance().removeClass( Dog.class );
		assertEquals( 0, getClassExplorer().getTable().getItems().length );
		
		God.getInstance().addClass( Dog.class );
		God.getInstance().addClass( Person.class );
		items = getClassExplorer().getTable().getItems();;
		assertEquals( Dog.class, items[0].getData() ) ;
		assertEquals( "Dog", items[0].getText() );
		assertEquals( 2, items.length );
		assertEquals( Person.class, items[1].getData() ) ;
		assertEquals( "Person", items[1].getText() );
	}
	
	/**
	 * 8:
	 * So to recap we now have a class explorer view that dynamically responds
	 * to changes to the metamodel.  Cool.
	 * What might we want to do now? 
	 * REQUIREMENT : open an editor when a class entry in the ClassExplorerView 
	 * is double-clicked.  We're not yet even defining what editor.
	 * TEST: check an editor opens when a class entry is dbl-clicked.
	 * IMPLEMENTATION :
	 * 1. added package mikespike2_runtime.editors - ensure this is exported 
	 * by plugin
	 * 2. added DefaultEditor (java class & plugin declaration)
	 * 3. added DefaultEditorInput - this is passed an instance of one of the
	 * domain classes known to God
	 * 4. added method to God to produce this instance
	 * 5. added a mouse listener to the viewer to open the editor
	 * ISSUES : errors can leave editors open for later tests so override
	 * this Test class's tearDown() method
	 */
	public void testOpenArbitaryEditor() {
		// can have multiple editors so must do count, not a null check
		assertEquals( 0, TestUtil.getOpenEditors().length );
		
		// nothing selected as God is empty, however no harm in checking
		TableViewer viewer = getClassExplorer();
		assertTrue( viewer.getSelection().isEmpty() );
		
		// do double click with nothing selected
		TestUtil.sendDoubleClick( viewer.getControl() );
		assertEquals( 0, TestUtil.getOpenEditors().length );
		
		// now do double click with something selected
		God.getInstance().addClass( Person.class );
		viewer.setSelection( new StructuredSelection( Person.class ) );
		// testing platform - a little redundant!
		assertEquals( Person.class,
				      ((StructuredSelection)viewer.getSelection()).getFirstElement() );
		TestUtil.sendDoubleClick( viewer.getControl() );
		assertEquals( 1, TestUtil.getOpenEditors().length );
		
		// clear everything (no need to test)
		TestUtil.getActivePage().closeAllEditors( false );
		
		// prove multiple editors can be opened
		int num = 5;
		for ( int i=0 ; i < num ; i++ ) {
			TestUtil.sendDoubleClick( viewer.getControl() );
		}
		assertEquals( 5, TestUtil.getOpenEditors().length );
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		TestUtil.getActivePage().closeAllEditors( false );
	}
}
