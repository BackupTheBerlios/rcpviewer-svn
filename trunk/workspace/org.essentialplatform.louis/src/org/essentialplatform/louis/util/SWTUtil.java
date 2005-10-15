package org.essentialplatform.louis.util;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Miscellaneous UI utilities.
 */
public class SWTUtil {
	      
    /**
     * Expands the last column in a table to suck up any left over space in 
     * it's table.
     * @param table
     */
	public static void autosizeLastColumn(Table table) {
    	if ( table == null ) throw new IllegalArgumentException();
        autosizeColumn(table, table.getColumns().length - 1);
    }
    
    /**
     * Expands the last column in a table to suck up any left over space in 
     * it's table.
     * @param tree
     */
    public static void autosizeLastColumn(Tree tree) {
    	if ( tree == null ) throw new IllegalArgumentException();
        autosizeColumn( tree, tree.getColumns().length - 1);
    }

    /**
     * Expands the denoted column in a table to suck up any left over space in 
     * it's table.
     * @param table
     * @param column index
     */
    static public void autosizeColumn(Table table, int columnIndex) {
    	if( table == null ) throw new IllegalArgumentException();
    	if( columnIndex < 0 ) throw new IllegalArgumentException();
    	if( columnIndex >= table.getColumnCount() ) throw new IllegalArgumentException();
        TableColumn[] columns = table.getColumns();
        if (0 < columns.length) {
            int totColumnWidth = 0;
            for (int i = 0; i < columns.length; i++) 
                if (i != columnIndex)
                    totColumnWidth += columns[i].getWidth();

            int clientWidth = table.getClientArea().width;
            clientWidth -= table.getBorderWidth() * 2;
            int lastColWidth = clientWidth - totColumnWidth;
            TableColumn lastColumn = columns[columnIndex];
            if (lastColumn.getWidth() < lastColWidth)
                lastColumn.setWidth(lastColWidth);
        }
    }
    
    /**
     * Expands the denoted column in a tree to suck up any left over space in 
     * it's tree.
     * @param tree
     * @param column index
     */
    static public void autosizeColumn(Tree tree, int columnIndex) {
    	if ( tree == null ) throw new IllegalArgumentException();
    	if ( columnIndex < 0 ) throw new IllegalArgumentException();
    	if ( columnIndex >= tree.getColumnCount() ) throw new IllegalArgumentException();
        TreeColumn[] columns = tree.getColumns();
        if (0 < columns.length) {
            int totColumnWidth = 0;
            for (int i = 0; i < columns.length; i++) 
                if (i != columnIndex)
                    totColumnWidth += columns[i].getWidth();
            int clientWidth = tree.getClientArea().width;
            clientWidth -= tree.getBorderWidth() * 2;
            int lastColWidth = clientWidth - totColumnWidth;
            TreeColumn lastColumn = columns[columnIndex];
            if (lastColumn.getWidth() < lastColWidth)
                lastColumn.setWidth(lastColWidth);
        }
    }

    
    /**
     * Generates what the value in the passed <code>Text</code> would be if the
     * passed <code>VerifyEvent</code> is applied
     * @param event
     * @return
     */
    public static String buildResultantText( Text text, VerifyEvent event ) {
    	if ( text == null ) throw new IllegalArgumentException() ;
    	if ( event == null ) throw new IllegalArgumentException() ;
    	
		String orig = text.getText();
		if ( orig.length() == 0 ) {
			return event.text;
		}
		else {
			StringBuffer sb = new StringBuffer();
			sb.append( orig.substring( 0, event.start ) );
			sb.append( event.text );
			sb.append( orig.substring( event.end, orig.length() ) );
			return sb.toString();
		}
    }
    
    /**
     * Finds first parent of the passed class or <code>null</code>
     * @param control
     * @param clazz
     * @return
     */
    public static <T extends Composite> T getParent( Control control, Class clazz ) {
    	if ( control == null ) throw new IllegalArgumentException() ;
    	if ( clazz == null ) throw new IllegalArgumentException() ;
    	Composite parent = control.getParent();
    	while ( parent != null ) {
    		if ( parent.getClass().equals( clazz ) ) return (T)parent;
    		parent = parent.getParent();
    	}
    	return null;
    }
    
    /**
     * Sets enablement of all controls on the passed composite.  Recurses as
     * necessary.
     * @param parent
     * @param enabled
     */
    public static void setEnabled( Composite parent, boolean enabled ) {
    	if ( parent == null ) throw new IllegalArgumentException() ;
    	for ( Control child : parent.getChildren() ) {
    		if ( child instanceof Composite ) {
    			setEnabled( (Composite)child, enabled );
    		}
    		else {
    			child.setEnabled( enabled );
    		}
    	}
    }
}
  


 
    
 

 



  

