package net.sf.plugins.utils;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * Miscellaneous UI utilities.
 */
public class SWTUtils {
	   
    /**
     * Returns true of the given control or any of it's children has the focus.
     */
    public static boolean isFocusAncestor(Control control) {
        Display display= control.getDisplay();
        Control focusControl= display.getFocusControl();
        while (focusControl != null) {
            if (focusControl == control) 
                return true;
            focusControl= focusControl.getParent();
        }
        return false; 
    }

    /**
     * Resizes all the columns in a table to fit the column's contents, 
     * including the column header.
     * 
     * Callers should use the Table.setRedraw method before calling this method in 
     * order to avoid flicker.
     * 
     * WARNING: Do not call this method from within a table resize event.
     * This method may cause the table to resize, therefore calling this 
     * method from within a table resize event may cause a stack overflow.
     * 
     */
    static public void packTableColumns(Table table) {
        TableColumn[] columns= table.getColumns();
        int columnWidth = 0;
        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            int minWidth = column.getWidth();
        
            column.pack();
            if ((column.getWidth() < minWidth) && (i < columns.length - 1))
                column.setWidth(minWidth);
            if (i < columns.length - 1)
                columnWidth += column.getWidth();
        }
    }

    static public void packTableColumns(Table table, int row) {
        TableColumn[] columns = table.getColumns();
        int columnWidth = 0;
        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            int minWidth = column.getWidth();
            column.pack();
            if (column.getWidth() < minWidth)
                column.setWidth(minWidth);
            if (i < columns.length - 1)
                columnWidth += column.getWidth();
        }
    }

    
    /**
     * Evenly expands column widths to suck up any leftover space in the table.
     * Does not expand a column past it's packed width.
     * 
     * This method should only be called on hidden tables since it will 
     * cause crazy insane flicker otherwise. 
     * @param table
     */
    static public void evenlyWidenColumns(Table table) {
    	if ( table == null ) throw new IllegalArgumentException();
        TableColumn[] columns = table.getColumns();
        if (0 < columns.length) {
            int clientWidth = table.getClientArea().width;
            clientWidth -= table.getBorderWidth() * 2;

            int totStartWidth = 0;
            int[] startWidths= new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
                TableColumn column= columns[i]; 
                totStartWidth += startWidths[i]= column.getWidth();
                column.pack();
            }
            
            if (clientWidth <= totStartWidth) { 
                return;
            }
            int[] packedWidths= new int[columns.length];
            int totPackedWidth = 0;
            for (int i = 0; i < columns.length; i++) {
                TableColumn column= columns[i]; 
                int width= column.getWidth();
                if (width < startWidths[i]) 
                    column.setWidth(width= startWidths[i]);
                totPackedWidth += packedWidths[i]= width;
            }
            
            if (totPackedWidth < clientWidth) {
                autosizeLastColumn(table);
                return;
            }
            
            int totExcess= totPackedWidth - clientWidth;
            int totDiff= totPackedWidth - totStartWidth;
            int totFinalWidth= 0;
            for (int i = 0; i < columns.length; i++) {
                TableColumn column= columns[i];
                int packedWidth= packedWidths[i];
                int diff= packedWidth - startWidths[i];
                int excess= (totExcess * diff) / totDiff;
                if (0 < excess) {
                    int w= packedWidth - excess;
                    totFinalWidth+= w;
                    column.setWidth(w);
                }
            }
        }
    }
    
    /**
     * Evenly expands column widths to suck up any leftover space in the table.
     * Does not expand a column past it's packed width.
     * 
     * This method should only be called on hidden tables since it will 
     * cause crazy insane flicker otherwise. 
     * @param tree
     */
    static public void evenlyWidenColumns(Tree tree) {
    	if ( tree == null ) throw new IllegalArgumentException();
        TreeColumn[] columns = tree.getColumns();
        if (0 < columns.length) {
            int clientWidth = tree.getClientArea().width;
            clientWidth -= tree.getBorderWidth() * 2;

            int totStartWidth = 0;
            int[] startWidths= new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
            	TreeColumn column= columns[i]; 
                totStartWidth += startWidths[i]= column.getWidth();
                column.pack();
            }
            
            if (clientWidth <= totStartWidth) { 
                return;
            }
            int[] packedWidths= new int[columns.length];
            int totPackedWidth = 0;
            for (int i = 0; i < columns.length; i++) {
            	TreeColumn column= columns[i]; 
                int width= column.getWidth();
                if (width < startWidths[i]) 
                    column.setWidth(width= startWidths[i]);
                totPackedWidth += packedWidths[i]= width;
            }
            
            if (totPackedWidth < clientWidth) {
                autosizeLastColumn(tree);
                return;
            }
            
            int totExcess= totPackedWidth - clientWidth;
            int totDiff= totPackedWidth - totStartWidth;
            int totFinalWidth= 0;
            for (int i = 0; i < columns.length; i++) {
            	TreeColumn column= columns[i];
                int packedWidth= packedWidths[i];
                int diff= packedWidth - startWidths[i];
                int excess= (totExcess * diff) / totDiff;
                if (0 < excess) {
                    int w= packedWidth - excess;
                    totFinalWidth+= w;
                    column.setWidth(w);
                }
            }
        }
    }
        

    /**
     * Expands the last column in a table to suck up any left over space in 
     * it's table.
     * @param table
     */
    static public void autosizeLastColumn(Table table) {
    	if ( table == null ) throw new IllegalArgumentException();
        autosizeColumn(table, table.getColumns().length - 1);
    }
    
    /**
     * Expands the last column in a table to suck up any left over space in 
     * it's table.
     * @param tree
     */
    static public void autosizeLastColumn(Tree tree) {
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
     * Centers a shell on it's Display
     */
    static public void center(Shell shell) {
        Display display = shell.getDisplay();
        Rectangle bounds = display.getBounds ();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 3;
        shell.setLocation(x, y);
    }
    
    /**
     * Looks for all input elements (text fields, checkboxes, etc) and disables them.
     * Recurses through all children.
     */
    public static void setReadOnlyOnAllInputElements(Widget parent, boolean value) {
        if (isInputElement(parent))
            setEnabled(parent, value);
        if (parent instanceof Composite) {
            Control[] children= ((Composite)parent).getChildren();
            for (int i= 0; i < children.length; i++) {
                setReadOnlyOnAllInputElements(children[i], value);
            }
        }
    }
    
    private static boolean isInputElement(Widget widget) {
        if (widget instanceof Text) 
            return true;
        if (widget instanceof Combo)
            return true;
        if (widget instanceof Button)
            return true;
        if (widget instanceof Menu)
            return true;
        return false;
    }
    
    private static void setEnabled(Widget widget, boolean value) {
        if (widget instanceof Control)
            ((Control)widget).setEnabled(value);
        if (widget instanceof Menu)
            ((Menu)widget).setEnabled(value);
    }
    
    public static final boolean isDescendant(Composite parent, Control target) {
        if (parent == target)
            return true;
        Control[] children= parent.getChildren();
        for (int i= 0; i < children.length; i++) {
            Control child= children[i];
            if (target == child)
                return true;
            if (child instanceof Composite) {
                if (isDescendant((Composite)child, target))
                    return true;
            }
        }
        return false;
    }

    /**
     * Positions the pulldown control underneath the bottom lefthand corner of the 
     * chiclet control.  However, the pulldown control position will be adjusted 
     * so that no part of it is off the display.
     * @param newShell
     * @param control
     */
    public static void positionPulldown(Control pulldown, Control chiclet) {
        Point p= chiclet.getLocation();
        p.y+= chiclet.getSize().y;
        pulldown.setLocation(p);
    }
       

    
}
  


 
    
 

 



  

